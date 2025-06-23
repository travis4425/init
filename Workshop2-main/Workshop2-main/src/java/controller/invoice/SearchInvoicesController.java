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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SearchInvoicesController - Xử lý tìm kiếm hóa đơn (chỉ admin)
 */
@WebServlet(name = "SearchInvoicesController", urlPatterns = {"/SearchInvoicesController"})
public class SearchInvoicesController extends HttpServlet {

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
            searchInvoices(request, response, loginUser);
        } catch (Exception e) {
            log("Error at SearchInvoicesController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi: " + e.getMessage());
            response.sendRedirect("MainController?action=ViewInvoices");
        }
    }

    // Method tìm kiếm hóa đơn
    private void searchInvoices(HttpServletRequest request, HttpServletResponse response, User user)
            throws Exception {

        // Chỉ admin mới được tìm kiếm
        if (!"AD".equals(user.getRoleID())) {
            request.setAttribute("MSG", "Bạn không có quyền thực hiện chức năng này.");
            response.sendRedirect("MainController?action=ViewInvoices");
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
            response.sendRedirect("MainController?action=ViewInvoices");
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

    @Override
    public String getServletInfo() {
        return "Search Invoices Controller";
    }
}