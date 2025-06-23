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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ListInvoicesController - Xử lý hiển thị danh sách hóa đơn
 */
@WebServlet(name = "ListInvoicesController", urlPatterns = {"/ListInvoicesController"})
public class ListInvoicesController extends HttpServlet {

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
            listInvoices(request, response, loginUser);
        } catch (Exception e) {
            log("Error at ListInvoicesController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // Danh sách hóa đơn
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
            request.getRequestDispatcher("invoiceList.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in listInvoices: " + e.getMessage());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            request.getRequestDispatcher("invoiceList.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "List Invoices Controller";
    }
}