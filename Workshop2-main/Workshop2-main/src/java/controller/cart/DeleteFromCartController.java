/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.cart;

import dao.CartDAO;
import dao.CartDetailDAO;
import dto.Cart;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name="DeleteFromCartController", urlPatterns={"/DeleteFromCartController"})
public class DeleteFromCartController extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String userID = loginUser.getUserID();
        try {
            int productID = Integer.parseInt(request.getParameter("productID"));
            
            // Tìm cartID của người dùng
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCartByUserID(userID);
            
            if (cart != null) {
                CartDetailDAO detailDAO = new CartDetailDAO();
                detailDAO.deleteCartDetail(cart.getCartID(), productID);
            }
            
            request.setAttribute("MSG", "Delete successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("MSG", "Delete Fail!");
        }
        // Chuyển về trang giỏ hàng
        request.getRequestDispatcher("ViewCartController").forward(request, response);
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
        return "Short description";
    }// </editor-fold>

}
