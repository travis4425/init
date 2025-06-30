/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ProductsControllers;

import dao.ProductDAO;
import dao.PromotionDAO;
import dto.Product;
import dto.Promotion;
import dto.User;
import dto.Category;
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
@WebServlet(name = "SearchProductController", urlPatterns = {"/SearchProductController"})
public class SearchProductController extends HttpServlet {

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
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        try (java.sql.Connection conn = DBUtils.getConnection()) {
            String nameSearch = request.getParameter("nameSearch") != null ? request.getParameter("nameSearch") : "";
            String cateSearch = request.getParameter("cateSearch") != null ? request.getParameter("cateSearch") : "";
            Float priceSearch = null;
            if (request.getParameter("priceSearch") != null && !request.getParameter("priceSearch").isEmpty()) {
                try {
                    priceSearch = Float.parseFloat(request.getParameter("priceSearch"));
                } catch (NumberFormatException e) {
                    priceSearch = 0f;
                }
            } else {
                priceSearch = 0f;
            }
            String statusSearch = request.getParameter("statusSearch") != null ? request.getParameter("statusSearch") : "";
            
            // Thêm log để debug
            log("Parameters for role " + loginUser.getRoleID() + ": nameSearch=" + nameSearch + 
                ", cateSearch=" + cateSearch + ", priceSearch=" + priceSearch + 
                ", statusSearch=" + statusSearch);
            
            ProductDAO dao = new ProductDAO();
            List<Product> list;
            if ("AD".equals(loginUser.getRoleID()) || "BU".equals(loginUser.getRoleID()) || "MK".equals(loginUser.getRoleID())) {
                list = dao.search(nameSearch, cateSearch, priceSearch, statusSearch);
            } else {
                list = dao.getProductsByUser(loginUser.getUserID(), nameSearch, cateSearch, priceSearch, statusSearch);
            } 
            List<Category> categories = dao.getAllCategories();
            
            // Lấy danh sách khuyến mãi
            PromotionDAO promoDao = new PromotionDAO(conn);
            List<Promotion> promotionList = promoDao.getAllPromotions();
            
            request.setAttribute("list", list);
            request.setAttribute("nameSearch", nameSearch);
            request.setAttribute("cateSearch", cateSearch);
            request.setAttribute("priceSearch", priceSearch);
            request.setAttribute("statusSearch", statusSearch);
            request.setAttribute("categories", categories);
            request.setAttribute("PROMOTION_LIST", promotionList);
            request.getRequestDispatcher("product/productList.jsp").forward(request, response);
        }catch (Exception e) {
            log("Error in SearchProductController: " + e.getMessage(), e);
            request.setAttribute("ERROR", "An error occurred while searching product: " + e.getMessage());
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
