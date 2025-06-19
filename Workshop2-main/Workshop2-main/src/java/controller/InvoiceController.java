package controller;

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
        if (action == null) action = "list";
        
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
                case "cancel":
                    cancelInvoice(request, response, loginUser);
                    break;
                default:
                    listInvoices(request, response, loginUser);
                    break;
            }
        } catch (Exception e) {
            log("Error at InvoiceController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            listInvoices(request, response, loginUser);
        }
    }
    
    // Thanh toán từ giỏ hàng
    private void checkout(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        
        int cartID = cartDAO.getCurrentCartID(user.getUserID());
        
        // Kiểm tra giỏ hàng có sản phẩm không
        Map<Product, Integer> cartItems = cartDetailDAO.getCartWithProducts(cartID);
        if (cartItems.isEmpty()) {
            request.setAttribute("MSG", "Giỏ hàng trống, không thể thanh toán.");
            response.sendRedirect("CartController?action=view");
            return;
        }
        
        // Kiểm tra số lượng sản phẩm trong kho
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int requestedQuantity = entry.getValue();
            
            if (!productDAO.checkProductAvailability(product.getProductID(), requestedQuantity)) {
                request.setAttribute("MSG", "Sản phẩm " + product.getName() + " không đủ số lượng trong kho.");
                response.sendRedirect("CartController?action=view");
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
                response.sendRedirect("CartController?action=view");
            }
        } else {
            request.setAttribute("MSG", "Không thể tạo hóa đơn.");
            response.sendRedirect("CartController?action=view");
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
            if (!invoice.getUserID().equals(user.getUserID()) && !"admin".equals(user.getRole())) {
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
    private void listInvoices(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        
        List<Invoice> invoices;
        
        if ("admin".equals(user.getRole())) {
            // Admin xem tất cả hóa đơn
            invoices = invoiceDAO.getAllInvoices();
        } else {
            // User chỉ xem hóa đơn của mình
            invoices = invoiceDAO.getInvoicesByUser(user.getUserID());
        }
        
        request.setAttribute("INVOICES", invoices);
        request.getRequestDispatcher("invoiceList.jsp").forward(request, response);
    }
    
    // Hủy hóa đơn (chỉ với trạng thái Pending)
    private void cancelInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {
        
        String invoiceIDStr = request.getParameter("invoiceID");
        if (invoiceIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy hóa đơn cần hủy.");
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
            
            // Kiểm tra quyền hủy hóa đơn
            if (!invoice.getUserID().equals(user.getUserID()) && !"admin".equals(user.getRole())) {
                request.setAttribute("MSG", "Bạn không có quyền hủy hóa đơn này.");
                listInvoices(request, response, user);
                return;
            }
            
            // Chỉ hủy được hóa đơn có trạng thái Pending
            if (!"Pending".equals(invoice.getStatus())) {
                request.setAttribute("MSG", "Chỉ có thể hủy hóa đơn đang chờ xử lý.");
                listInvoices(request, response, user);
                return;
            }
            
            // Cập nhật trạng thái hóa đơn
            boolean success = invoiceDAO.updateInvoiceStatus(invoiceID, "Cancelled");
            
            if (success) {
                // Hoàn trả số lượng sản phẩm về kho
                Map<Product, InvoiceDetail> invoiceDetails = invoiceDetailDAO.getInvoiceDetailsWithProducts(invoiceID);
                for (Map.Entry<Product, InvoiceDetail> entry : invoiceDetails.entrySet()) {
                    Product product = entry.getKey();
                    InvoiceDetail detail = entry.getValue();
                    
                    // Cập nhật lại số lượng sản phẩm (cộng lại số lượng đã bán)
                    String updateSQL = "UPDATE tblProducts SET quantity = quantity + ? WHERE productID = ?";
                    try (java.sql.Connection conn = utils.DBUtils.getConnection();
                         java.sql.PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                        ps.setInt(1, detail.getQuantity());
                        ps.setInt(2, product.getProductID());
                        ps.executeUpdate();
                    }
                }
                
                request.setAttribute("MSG", "Đã hủy hóa đơn thành công.");
            } else {
                request.setAttribute("MSG", "Không thể hủy hóa đơn.");
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