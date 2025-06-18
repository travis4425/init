/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.ProductDAO;
import dto.Product;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 *
 * @author Admin
 */
@WebServlet(name="CartController", urlPatterns={"/CartController"})
public class CartController extends HttpServlet {
    
    private CartDAO cartDAO = new CartDAO();
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private ProductDAO productDAO = new ProductDAO(); // Cần implement ProductDAO
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        
        // Kiểm tra đăng nhập
        if (loginUser == null) {
            response.sendRedirect("LoginController");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "view";
        
        try {
            switch (action) {
                case "add":
                    addToCart(request, response, loginUser);
                    break;
                case "update":
                    updateCart(request, response, loginUser);
                    break;
                case "remove":
                    removeFromCart(request, response, loginUser);
                    break;
                case "clear":
                    clearCart(request, response, loginUser);
                    break;
                case "view":
                default:
                    viewCart(request, response, loginUser);
                    break;
            }
        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
            request.setAttribute("MSG", "An error occurred while processing your cart.");
            viewCart(request, response, loginUser);
        }
    }
    
    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");
        
        if (productIDStr == null || quantityStr == null) {
            request.setAttribute("MSG", "Invalid product information.");
            viewCart(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                request.setAttribute("MSG", "Quantity must be greater than 0.");
                viewCart(request, response, user);
                return;
            }
            
            // Lấy hoặc tạo cart cho user
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            // Thêm vào giỏ hàng
            boolean success = cartDetailDAO.addToCart