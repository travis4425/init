package controller.invoice;

import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
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
import java.util.Map;

/**
 * ViewInvoiceController - Xử lý xem chi tiết hóa đơn
 */
@WebServlet(name = "ViewInvoiceController", urlPatterns = {"/ViewInvoiceController"})
public class ViewInvoiceController extends HttpServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        // Kiểm tra đăng nhập
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            viewInvoice(request, response, loginUser);
        } catch (Exception e) {
            log("Error at ViewInvoiceController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    // Xem chi tiết hóa đơn
    private void viewInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        String invoiceIDStr = request.getParameter("invoiceID");
        if (invoiceIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy hóa đơn.");
            response.sendRedirect("MainController?action=ViewInvoices");
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDStr);

            // Lấy thông tin hóa đơn
            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);

            if (invoice == null) {
                request.setAttribute("MSG", "Hóa đơn không tồn tại.");
                response.sendRedirect("MainController?action=ViewInvoices");
                return;
            }

            // Kiểm tra quyền xem hóa đơn (chỉ user sở hữu hoặc admin)
            if (!invoice.getUserID().equals(user.getUserID()) && !"AD".equals(user.getRoleID())) {
                request.setAttribute("MSG", "Bạn không có quyền xem hóa đơn này.");
                response.sendRedirect("MainController?action=ViewInvoices");
                return;
            }

            // Lấy chi tiết hóa đơn
            Map<Product, InvoiceDetail> invoiceDetails = invoiceDetailDAO.getInvoiceDetailsWithProducts(invoiceID);

            request.setAttribute("INVOICE", invoice);
            request.setAttribute("INVOICE_DETAILS", invoiceDetails);
            request.getRequestDispatcher("invoiceDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    @Override
    public String getServletInfo() {
        return "View Invoice Controller";
    }
}