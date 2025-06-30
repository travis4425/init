package controller.CustomerCare;

import dao.CustomerCareDAO;
import dto.CustomerCare;
import dto.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SearchCustomerCareController", urlPatterns = {"/SearchCustomerCareController"})
public class SearchCustomerCareController extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            request.setCharacterEncoding("UTF-8"); // ✅ Đảm bảo nhận ký tự Unicode
            String search = request.getParameter("search");
            if (search == null) search = "";

            List<CustomerCare> list; 
            CustomerCareDAO dao = new CustomerCareDAO();
            if ("BU".equals(loginUser.getRoleID())) {
                list = dao.getCustomerCareByUser(loginUser.getUserID()); // lấy của đúng user đó
            } else {
                list = dao.getCustomerCareList(search); // Lấy tất cả ticket
            } 

            request.setAttribute("listCare", list);
        } catch (Exception e) {
            log("ERROR at SearchCustomerCareController: " + e.toString());
        } finally {
            request.getRequestDispatcher("customer_care/CustomerCare.jsp").forward(request, response);
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
}
