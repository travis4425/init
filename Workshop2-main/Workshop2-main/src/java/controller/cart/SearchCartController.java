
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.Cart;

import dto.User;
import dto.Cart;
import dao.CartDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ACER
 */
@WebServlet(name = "SearchCartController", urlPatterns = {"/SearchCartController"})
public class SearchCartController extends HttpServlet {

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
            String keyword = request.getParameter("search") != null ? request.getParameter("search") : "";
            CartDAO dao = new CartDAO();
            List<Cart> list = dao.search(keyword);
            request.setAttribute("keyword", keyword);
            request.setAttribute("list", list);
            request.getRequestDispatcher("Cart/cartList.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in SearchCartController: " + e.getMessage());
            request.setAttribute("ERROR", "An error occurred while searching cart: " + e.getMessage());
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
}
