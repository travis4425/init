/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.cart;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.ProductDAO;
import dto.Cart;
import dto.CartDetail;
import dto.Product;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name="AddToCartController", urlPatterns={"/AddToCartController"})
public class AddToCartController extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
        if (loginUser == null) {
            log("No logged-in user found, redirecting to login.jsp");
            response.sendRedirect("login.jsp");
            return;
        }
        String userID = loginUser.getUserID();
        try {
            int productID = Integer.parseInt(request.getParameter("id"));
            
            // Lấy thông tin sản phẩm
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductById(productID);
            
            if (product == null) {
                request.setAttribute("MSG", "Sản phẩm không tồn tại.");
                request.getRequestDispatcher("SearchProductController").forward(request, response);
                return;
            }
            if (product.getQuantity() <= 0) {
                request.setAttribute("MSG", "Sản phẩm đã hết hàng.");
                request.getRequestDispatcher("SearchProductController").forward(request, response);
                return;
            }

            // Lấy hoặc tạo giỏ hàng
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCartByUserID(userID);
            if (cart == null) {
                cart = new Cart();
                cart.setUserID(userID);
                cart.setCreatedDate(LocalDate.now());
                int newCartID = cartDAO.insertCart(cart);
                cart.setCartID(newCartID);
            }
            
            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ chưa
            CartDetailDAO detailDAO = new CartDetailDAO();
            List<CartDetail> currentItems = detailDAO.getCartDetails(cart.getCartID());
            boolean found = false;

            for (CartDetail item : currentItems) {
                if (item.getProduct().getProductID() == productID) {
                    int currentQuantity = item.getQuantity();
                    if (currentQuantity + 1 > product.getQuantity()) {
                        request.setAttribute("MSG", "Không đủ hàng trong kho.");
                        request.getRequestDispatcher("SearchProductController").forward(request, response);
                        return;
                    }   
                    detailDAO.updateQuantity(cart.getCartID(), productID, currentQuantity + 1);
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                detailDAO.insertCartDetail(new CartDetail(cart.getCartID(), product, 1));
            }
            
            request.setAttribute("MSG", "Added To Cart Successfully!");
            request.getRequestDispatcher("SearchProductController").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("MSG", "Added To Cart Failed!");
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
        return "Short description";
    }// </editor-fold>

}
