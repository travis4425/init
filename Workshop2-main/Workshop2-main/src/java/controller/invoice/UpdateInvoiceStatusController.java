package controller.invoice;

import dao.InvoiceDAO;
import dto.Invoice;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UpdateInvoiceStatusController - Xử lý cập nhật trạng thái hóa đơn (chỉ admin)
 */
@WebServlet(name = "UpdateInvoiceStatusController", urlPatterns = {"/UpdateInvoiceStatusController"})
public class UpdateInvoiceStatusController extends HttpServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();

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
            updateInvoiceStatus(request, response, loginUser);
        } catch (Exception e) {
            log("Error at UpdateInvoiceStatusController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    // Method cập nhật trạng thái hóa đơn
    private void updateInvoiceStatus(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được cập nhật trạng thái
        if (!"AD".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            response.sendRedirect("MainController?action=ViewInvoices");
            return;
        }

        String invoiceIDStr = request.getParameter("invoiceID");
        String newStatus = request.getParameter("status");

        if (invoiceIDStr == null || newStatus == null) {
            request.setAttribute("MSG", "Thông tin không hợp lệ.");
            response.sendRedirect("MainController?action=ViewInvoices");
            return;
        }

        try {
            int invoiceID = Integer.parseInt(invoiceIDStr);

            // Kiểm tra hóa đơn tồn tại
            Invoice invoice = invoiceDAO.getInvoiceByID(invoiceID);
            if (invoice == null) {
                request.setAttribute("MSG", "Hóa đơn không tồn tại.");
                response.sendRedirect("MainController?action=ViewInvoices");
                return;
            }

            // Cập nhật trạng thái
            boolean success = invoiceDAO.updateInvoiceStatus(invoiceID, newStatus);

            if (success) {
                request.setAttribute("MSG", "Đã cập nhật trạng thái hóa đơn thành công.");
            } else {
                request.setAttribute("MSG", "Không thể cập nhật trạng thái hóa đơn.");
            }

            response.sendRedirect("MainController?action=ViewInvoices");

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    @Override
    public String getServletInfo() {
        return "Update Invoice Status Controller";
    }
}