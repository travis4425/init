package controller.invoice;

import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
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
 * DeleteInvoiceController - Xử lý xóa hóa đơn (chỉ admin)
 */
@WebServlet(name = "DeleteInvoiceController", urlPatterns = {"/DeleteInvoiceController"})
public class DeleteInvoiceController extends HttpServlet {

    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            deleteInvoice(request, response, loginUser);
        } catch (Exception e) {
            log("Error at DeleteInvoiceController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được xóa
        if (!"AD".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            response.sendRedirect("MainController?action=ViewInvoices");
            return;
        }

        String invoiceIDStr = request.getParameter("invoiceID");
        if (invoiceIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy hóa đơn cần xóa.");
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

            // Xóa chi tiết hóa đơn trước
            invoiceDetailDAO.deleteAllInvoiceDetails(invoiceID);

            // Xóa hóa đơn
            boolean success = invoiceDAO.deleteInvoice(invoiceID);

            if (success) {
                request.setAttribute("MSG", "Đã xóa hóa đơn thành công.");
            } else {
                request.setAttribute("MSG", "Không thể xóa hóa đơn.");
            }

            response.sendRedirect("MainController?action=ViewInvoices");

        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID hóa đơn không hợp lệ.");
            response.sendRedirect("MainController?action=ViewInvoices");
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
        return "Delete Invoice Controller";
    }
}
