/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.CategoriesControllers;

import dto.User;
import dto.Category;
import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ACER
 */
@WebServlet(name = "CreateCategoryController", urlPatterns = {"/CreateCategoryController"})
public class CreateCategoryController extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                log("No logged-in user found, redirecting to login.jsp");
                response.sendRedirect("login.jsp");
                return;
            }
            CategoryDAO dao = new CategoryDAO();
            String categoryName = request.getParameter("categoryName");
            String description = request.getParameter("description");
            String keyword = request.getParameter("cateSearch") != null ? request.getParameter("cateSearch") : "";
            if (categoryName == null || categoryName.isEmpty() || description == null || description.isEmpty()) {
                request.setAttribute("MSG", "Please enter information!!");
                setCategoryListAttributes(request, loginUser, keyword);
                request.getRequestDispatcher("SearchCategoryController").forward(request, response);
                return;
            }
            if (dao.createCategory(categoryName, description)) {
                request.setAttribute("MSG", "Category created successfully.");
            } else {
                request.setAttribute("MSG", "Failed to create category.");
            }
            setCategoryListAttributes(request, loginUser, keyword);
            request.getRequestDispatcher("SearchCategoryController").forward(request, response);
        } catch (Exception e) {
            log("Error in CreateCategoryController: " + e.getMessage(), e);
            setCategoryListAttributes(request, (User) request.getSession().getAttribute("LOGIN_USER"), "");
            request.getRequestDispatcher("SearchCategoryController").forward(request, response);
        }
    }

    private void setCategoryListAttributes(HttpServletRequest request, User loginUser, String keyword) {
        try {
            CategoryDAO dao = new CategoryDAO();
            List<Category> list = dao.search(keyword);
            request.setAttribute("list", list);
            request.setAttribute("keyword", keyword);
        } catch (Exception e) {
            log("Error in setCategoryListAttributes: " + e.getMessage(), e);
            request.setAttribute("list", new ArrayList<Category>());
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