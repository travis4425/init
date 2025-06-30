package controller.CustomerCare;

import dao.CustomerCareDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateCustomerCareController", urlPatterns = {"/CreateCustomerCareController"})
public class CreateCustomerCareController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            String userID = loginUser.getUserID(); // Đã là int rồi nhé
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");

            CustomerCareDAO dao = new CustomerCareDAO();
            boolean result = dao.createCustomerCare(userID, subject, content);

            request.setAttribute("MSG", result ? "Created successfully!" : "Failed to create!");
            // Forward về SearchCustomerCareController** để load lại listCare
            request.getRequestDispatcher("SearchCustomerCareController").forward(request, response);

        } catch (Exception e) {
            log("ERROR at CreateCustomerCareController: " + e.toString());
            response.sendError(500);
        }
    }
}
