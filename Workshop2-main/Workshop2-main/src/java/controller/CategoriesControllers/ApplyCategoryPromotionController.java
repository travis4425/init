/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.CategoriesControllers;

import dao.CategoryDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.DBUtils;

@WebServlet(name = "ApplyCategoryPromotionController", urlPatterns = {"/ApplyCategoryPromotionController"})
public class ApplyCategoryPromotionController extends HttpServlet {

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
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            int promoID = Integer.parseInt(request.getParameter("promoID"));

            // Gọi DAO
            try ( java.sql.Connection conn = DBUtils.getConnection()) {
                CategoryDAO dao = new CategoryDAO();
                boolean result = dao.applyPromotion(categoryID, promoID);

                request.setAttribute("MSG", result ? "Promotion applied successfully to category!" : "Failed to apply promotion!");
            }

        } catch (Exception e) {
            log("Error at ApplyCategoryPromotionController: " + e.toString(), e);
            request.setAttribute("MSG", "Error occurred while applying promotion: " + e.getMessage());
        } finally {
            request.getRequestDispatcher("SearchCategoryController").forward(request, response);
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
