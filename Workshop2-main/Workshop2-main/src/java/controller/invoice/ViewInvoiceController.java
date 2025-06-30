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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ViewInvoiceController", urlPatterns = {"/ViewInvoiceController"})
public class ViewInvoiceController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String userID = loginUser.getUserID();
        List<Invoice> invoices = new ArrayList<>();
        
        try {
            InvoiceDAO invoiceDAO = new InvoiceDAO();

            if ("AD".equals(loginUser.getRoleID())) {
                // Admin: xem tất cả yêu cầu
                invoices = invoiceDAO.getAllInvoices();
            } else {
                // Buyer: chỉ xem các yêu cầu liên quan đến hóa đơn của mình
                invoices = invoiceDAO.getInvoicesByUser(userID);
            }
            
            request.setAttribute("list", invoices);
            request.getRequestDispatcher("invoice/invoiceList.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("list", invoices);
            request.getRequestDispatcher("invoice/invoiceList.jsp").forward(request, response);
            e.printStackTrace();
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
        return "Invoice viewing and cancelling controller";
    }
}
