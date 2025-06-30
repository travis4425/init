/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.cart;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.InvoiceDAO;
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
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name="CheckoutController", urlPatterns={"/CheckoutController"})
public class CheckoutController extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String userID = loginUser.getUserID();
        
        String address = request.getParameter("address");
        
        try {
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.getCartByUserID(userID);
            
            if (cart == null || cart.getCartDetails().isEmpty()) {
                request.setAttribute("MSG", "Cart Empty");
                request.getRequestDispatcher("ViewCartController").forward(request, response);
                return;
            }
            
            // Kiểm tra số lượng tồn kho trước
            ProductDAO productDAO = new ProductDAO();
            for (CartDetail item : cart.getCartDetails()) {
                Product product = productDAO.getProductById(item.getProduct().getProductID());
                if (product.getQuantity() < item.getQuantity()) {
                    request.setAttribute("MSG", "Sản phẩm " + product.getName() + " không đủ hàng.");
                    request.getRequestDispatcher("ViewCartController").forward(request, response);
                    return;
                }
            }
            
            // Tạo hóa đơn
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            int invoiceID = invoiceDAO.createInvoice(userID, cart.getCartDetails());

            // Xóa giỏ hàng sau khi thanh toán
            CartDetailDAO detailDAO = new CartDetailDAO();
            for (CartDetail item : cart.getCartDetails()) {
                detailDAO.deleteCartDetail(cart.getCartID(), item.getProduct().getProductID());
            }
            
            // Tạo đơn giao hàng    
            dao.DeliveryDAO deliveryDAO = new dao.DeliveryDAO();
            deliveryDAO.createDelivery(invoiceID, address, "Pending");

            request.setAttribute("MSG", "Thanh toán thành công! Mã hóa đơn: " + invoiceID);
            request.getRequestDispatcher("ViewCartController").forward(request, response);
            
        } catch (Exception e) {
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
        return "Short description";
    }// </editor-fold>

}
