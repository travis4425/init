/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.PromotionsController;

import dao.ProductDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtils;

@WebServlet(name = "ApplyPromotionController", urlPatterns = {"/ApplyPromotionController"})
public class ApplyPromotionController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Kiểm tra đăng nhập và role
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null || !"MK".equals(loginUser.getRoleID())) {
                response.sendRedirect("login.jsp");
                return;
            }

            // Lấy tham số
            int productID = Integer.parseInt(request.getParameter("productID"));
            int promoID = Integer.parseInt(request.getParameter("promoID"));

            // Gọi DAO
            try ( java.sql.Connection conn = DBUtils.getConnection()) {
                ProductDAO dao = new ProductDAO();
                boolean result = dao.applyPromotion(productID, promoID);

                request.setAttribute("MSG", result ? "Promotion applied successfully!" : "Failed to apply promotion!");
            }

        } catch (Exception e) {
            log("Error at ApplyPromotionController: " + e.toString(), e);
            request.setAttribute("MSG", "Error occurred while applying promotion: " + e.getMessage());
        } finally {
            request.getRequestDispatcher("SearchProductController").forward(request, response);
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
