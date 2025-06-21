package controller.invoice;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.ProductDAO;
import dto.Invoice;
import dto.InvoiceDetail;
import dto.Product;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * InvoiceController - Xử lý các thao tác với hóa đơn
 */
@WebServlet(name = "InvoiceController", urlPatterns = {"/InvoiceController"})
public class InvoiceController extends HttpServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();
    private CartDAO cartDAO = new CartDAO();
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private ProductDAO productDAO = new ProductDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        // Kiểm tra đăng nhập
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "checkout":
                    checkout(request, response, loginUser);
                    break;
                case "view":
                    viewInvoice(request, response, loginUser);
                    break;
                case "list":
                    listInvoices(request, response, loginUser);
                    break;
                case "search":
                    searchInvoices(request, response, loginUser);
                    break;

// Action cập nhật trạng thái hóa đơn (cho admin)  
                case "updateStatus":
                    updateInvoiceStatus(request, response, loginUser);
                    break;

// Action xóa hóa đơn (cho admin)
                case "delete":
                    deleteInvoice(request, response, loginUser);
                    break;
                default:
                    listInvoices(request, response, loginUser);
                    break;
            }
        } catch (Exception e) {
            log("Error at InvoiceController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());

            // SỬA LỖI: Kiểm tra loginUser trước khi gọi listInvoices
            if (loginUser != null) {
                try {
                    listInvoices(request, response, loginUser);
                } catch (Exception ex) {
                    // Nếu listInvoices cũng lỗi, chuyển đến trang lỗi
                    log("Error in listInvoices fallback: " + ex.toString());
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
            } else {
                // Nếu loginUser null, chuyển về login
                response.sendRedirect("login.jsp");
            }
        }
    }

    // Thanh toán từ giỏ hàng - ĐÃ SỬA LỖI REDIRECT/FORWARD
    private void checkout(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        int cartID = cartDAO.getCurrentCartID(user.getUserID());

        // Kiểm tra giỏ hàng có sản phẩm không
        Map<Product, Integer> cartItems = cartDetailDAO.getCartWithProducts(cartID);
        if (cartItems.isEmpty()) {
            request.setAttribute("MSG", "Giỏ hàng trống, không thể thanh toán.");
            request.getRequestDispatcher("CartController?action=view").forward(request, response);
            return;
        }

        // Kiểm tra số lượng sản phẩm trong kho
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int requestedQuantity = entry.getValue();

            if (!productDAO.checkProductAvailability(product.getProductID(), requestedQuantity)) {
                request.setAttribute("MSG", "Sản phẩm " + product.getName() + " không đủ số lượng trong kho.");
                request.getRequestDispatcher("CartController?action=view").forward(request, response);
                return;
            }
        }

        // Tính tổng tiền
        float totalAmount = cartDetailDAO.getTotalAmount(cartID);

        // Tạo hóa đơn
        int invoiceID = invoiceDAO.createInvoice(user.getUserID(), totalAmount);

        if (invoiceID > 0) {
            // Tạo chi tiết hóa đơn từ giỏ hàng
            boolean detailsCreated = invoiceDetailDAO.createInvoiceDetailsFromCart(invoiceID, cartID);

            if (detailsCreated) {
                // Cập nhật số lượng sản phẩm trong kho
                for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                    Product product = entry.getKey();
                    int soldQuantity = entry.getValue();
                    productDAO.updateProductQuantity(product.getProductID(), soldQuantity);
                }

                // Xóa giỏ hàng sau khi thanh toán thành công
                cartDetailDAO.clearCart(cartID);

                // Chuyển đến trang thank you
                request.setAttribute("INVOICE_ID", invoiceID);
                request.setAttribute("TOTAL_AMOUNT", totalAmount);
                request.getRequestDispatcher("thankyou.jsp").forward(request, response);
            } else {
                request.setAttribute("MSG", "Không thể tạo chi tiết hóa đơn.");
                request.getRequestDispatcher("CartController?action=view").forward(request, response);
            }
        } else {
            request.setAttribute("MSG", "Không thể tạo hóa đơn.");
            request.getRequestDispatcher("CartController?action=view").forward(request, response);
        }
    }

    // Xem chi tiết hóa đơn
    private void viewInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        String invoiceIDStr = request.getParameter("invoiceID");
        if (invoiceIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy hóa đơn.");
            listInvoices(request, response, user);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDStr);

            // Lấy thông tin hóa đơn
            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);

            if (invoice == null) {
                request.setAttribute("MSG", "Hóa đơn không tồn tại.");
                listInvoices(request, response, user);
                return;
            }

            // Kiểm tra quyền xem hóa đơn (chỉ user sở hữu hoặc admin)
            if (!invoice.getUserID().equals(user.getUserID()) && !"admin".equals(user.getRoleID())) {
                request.setAttribute("MSG", "Bạn không có quyền xem hóa đơn này.");
                listInvoices(request, response, user);
                return;
            }

            // Lấy chi tiết hóa đơn
            Map<Product, InvoiceDetail> invoiceDetails = invoiceDetailDAO.getInvoiceDetailsWithProducts(invoiceID);

            request.setAttribute("INVOICE", invoice);
            request.setAttribute("INVOICE_DETAILS", invoiceDetails);
            request.getRequestDispatcher("invoiceDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            listInvoices(request, response, user);
        }
    }

    // Danh sách hóa đơn
    // Danh sách hóa đơn - ĐÃ SỬA LỖI
    private void listInvoices(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        try {
            List<Invoice> invoices = null;

            // Kiểm tra user không null
            if (user == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Kiểm tra roleID không null
            String roleID = user.getRoleID();
            if (roleID == null) {
                roleID = "user"; // default role
            }

            if ("AD".equals(roleID)) {
                // Admin xem tất cả hóa đơn
                invoices = invoiceDAO.getAllInvoices();
            } else {
                // User chỉ xem hóa đơn của mình
                String userID = user.getUserID();
                if (userID != null) {
                    invoices = invoiceDAO.getInvoicesByUser(userID);
                } else {
                    invoices = new ArrayList<>(); // Trả về danh sách rỗng nếu userID null
                }
            }

            // Kiểm tra invoices không null
            if (invoices == null) {
                invoices = new ArrayList<>();
            }

            request.setAttribute("INVOICES", invoices);
            request.getRequestDispatcher("invoiceList.jsp").forward(request, response);

        } catch (SQLException e) {
            log("Database error in listInvoices: " + e.getMessage());
            request.setAttribute("MSG", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            
        } catch (Exception e) {
            log("Error in listInvoices: " + e.getMessage());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
        }
    }
// Method tìm kiếm hóa đơn

    private void searchInvoices(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được tìm kiếm
        if (!"admin".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            listInvoices(request, response, user);
            return;
        }

        String userID = request.getParameter("userID");
        String status = request.getParameter("status");
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");

        LocalDate fromDate = null;
        LocalDate toDate = null;

        try {
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                fromDate = LocalDate.parse(fromDateStr);
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                toDate = LocalDate.parse(toDateStr);
            }
        } catch (Exception e) {
            request.setAttribute("MSG", "Định dạng ngày không hợp lệ.");
            listInvoices(request, response, user);
            return;
        }

        List<Invoice> invoices = invoiceDAO.searchInvoices(userID, status, fromDate, toDate);

        request.setAttribute("INVOICES", invoices);
        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("userID", userID != null ? userID : "");
        searchParams.put("status", status != null ? status : "");
        searchParams.put("fromDate", fromDateStr != null ? fromDateStr : "");
        searchParams.put("toDate", toDateStr != null ? toDateStr : "");

        request.setAttribute("SEARCH_PARAMS", searchParams);

        request.getRequestDispatcher("invoiceList.jsp").forward(request, response);
    }

// Method cập nhật trạng thái hóa đơn
    private void updateInvoiceStatus(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được cập nhật trạng thái
        if (!"admin".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            listInvoices(request, response, user);
            return;
        }

        String invoiceIDStr = request.getParameter("invoiceID");
        String newStatus = request.getParameter("status");

        if (invoiceIDStr == null || newStatus == null) {
            request.setAttribute("MSG", "Thông tin không hợp lệ.");
            listInvoices(request, response, user);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDStr);

            // Kiểm tra hóa đơn tồn tại
            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);
            if (invoice == null) {
                request.setAttribute("MSG", "Hóa đơn không tồn tại.");
                listInvoices(request, response, user);
                return;
            }

            // Cập nhật trạng thái
            boolean success = invoiceDAO.updateInvoiceStatus(invoiceID, newStatus);

            if (success) {
                request.setAttribute("MSG", "Đã cập nhật trạng thái hóa đơn thành công.");
            } else {
                request.setAttribute("MSG", "Không thể cập nhật trạng thái hóa đơn.");
            }

            listInvoices(request, response, user);

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            listInvoices(request, response, user);
        }
    }

// Method xóa hóa đơn (chỉ admin)
    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được xóa
        if (!"admin".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            listInvoices(request, response, user);
            return;
        }

        String invoiceIDStr = request.getParameter("invoiceID");
        if (invoiceIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy hóa đơn cần xóa.");
            listInvoices(request, response, user);
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDStr);

            // Kiểm tra hóa đơn tồn tại
            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);
            if (invoice == null) {
                request.setAttribute("MSG", "Hóa đơn không tồn tại.");
                listInvoices(request, response, user);
                return;
            }

            // Xóa chi tiết hóa đơn trước
            invoiceDetailDAO.deleteAllInvoiceDetails(invoiceID);

            // Xóa hóa đơn
            boolean success = invoiceDAO.deleteInvoice(invoiceID);

            if (success) {
                request.setAttribute("MSG", "Đã xóa hóa đơn thành công.");
            } else {
                request.setAttribute("MSG", "Không thể xóa hóa đơn.");
            }

            listInvoices(request, response, user);

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            listInvoices(request, response, user);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Invoice Controller";
    }
}
