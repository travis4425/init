/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dto.User;
import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author DELL
 */
@WebServlet(name = "CreateCartController", urlPatterns = {"/CreateCartController"})
public class CreateCartController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            CartDAO dao = new CartDAO();
            String userID = request.getParameter("userID");
            String createdDate = request.getParameter("createDate");

            if (userID == null || userID.trim().isEmpty()) {
                request.setAttribute("MSG", "User ID is required!");
                request.getRequestDispatcher("cartList.jsp").forward(request, response);
                return;
            }

            if (createdDate == null || createdDate.trim().isEmpty()) {
                createdDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }

            boolean isCreated = dao.createCart(userID, createdDate);
            if (isCreated) {
                request.setAttribute("MSG", "Cart created successfully!");
            } else {
                request.setAttribute("MSG", "Cart already exists or creation failed.");
            }
            request.getRequestDispatcher("cartList.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in CreateCartController: " + e.getMessage());
            request.setAttribute("ERROR", "An error occurred while creating cart: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
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
        return "CreateCartController handles cart creation requests";
    }
}
