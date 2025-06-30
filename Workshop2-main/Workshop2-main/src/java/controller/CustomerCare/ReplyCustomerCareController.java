/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.CustomerCare;

import dto.User;
import dao.CustomerCareDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "ReplyCustomerCareController", urlPatterns = {"/ReplyCustomerCareController"})
public class ReplyCustomerCareController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null || !"CS".equals(loginUser.getRoleID())) {
                response.sendRedirect("login.jsp");
                return;
            }

            int ticketID = Integer.parseInt(request.getParameter("ticketID"));
            String reply = request.getParameter("reply");

            CustomerCareDAO dao = new CustomerCareDAO();
            boolean result = dao.replyCustomerCare(ticketID, reply);

            request.setAttribute("MSG", result ? "Replied successfully!" : "Failed to reply!");
        } catch (Exception e) {
            log("ERROR at ReplyCustomerCareController: " + e.toString());
        } finally {           
            request.getRequestDispatcher("SearchCustomerCareController").forward(request, response);
        }
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}