/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.CategoriesControllers;

import dto.User;
import dto.Promotion;
import dao.CategoryDAO;
import dao.PromotionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author ACER
 */
@WebServlet(name = "SearchCategoryController", urlPatterns = {"/SearchCategoryController"})
public class SearchCategoryController extends HttpServlet {

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
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (!"AD".equals(loginUser.getRoleID()) && 
                                  !"MK".equals(loginUser.getRoleID()) &&   
                                  !"BU".equals(loginUser.getRoleID()) &&
                                  !"SE".equals(loginUser.getRoleID()))) {
            response.sendRedirect("login.jsp");
            return;
        }
        try ( java.sql.Connection conn = DBUtils.getConnection()) {
            String keyword = request.getParameter("cateSearch") != null ? request.getParameter("cateSearch") : "";
            CategoryDAO dao = new CategoryDAO();
            List list = dao.search(keyword);
            PromotionDAO promoDao = new PromotionDAO(conn);
            List<Promotion> promotionList = promoDao.getAllPromotions();
            request.setAttribute("keyword", keyword);
            request.setAttribute("list", list);
            request.setAttribute("PROMOTION_LIST", promotionList);
            request.getRequestDispatcher("category/categoryList.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in SearchCategoryController: " + e.getMessage());
            request.setAttribute("ERROR", "An error occurred while searching category: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
