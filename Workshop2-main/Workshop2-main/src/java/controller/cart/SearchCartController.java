/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import dao.CartDAO;
import dao.ProductDAO;
import dto.CartDetail;
import dto.Category;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name = "SearchCartController", urlPatterns = {"/SearchCartController"})
public class SearchCartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String userID = loginUser.getUserID();

        String nameSearch = request.getParameter("nameSearch");
        String cateSearch = request.getParameter("cateSearch");
        String priceStr = request.getParameter("priceSearch");

        Double maxPrice = null;
        try {
            if (priceStr != null && !priceStr.isEmpty()) {
                maxPrice = Double.parseDouble(priceStr);
            } else {
                maxPrice = null;
            }
            // Gọi DAO để tìm sản phẩm phù hợp trong giỏ hàng
            CartDAO cartDAO = new CartDAO();
            ProductDAO dao = new ProductDAO();
            List<CartDetail> searchResults = cartDAO.searchCart(userID, nameSearch, cateSearch, maxPrice);
            List<Category> categories = dao.getAllCategories();
            
            // Đưa dữ liệu về lại JSP
            request.setAttribute("categories", categories);
            request.setAttribute("list", searchResults);
            request.setAttribute("nameSearch", nameSearch);
            request.setAttribute("cateSearch", cateSearch);
            request.setAttribute("priceSearch", priceStr);

            request.getRequestDispatcher("cart/cartList.jsp").forward(request, response);
            
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
