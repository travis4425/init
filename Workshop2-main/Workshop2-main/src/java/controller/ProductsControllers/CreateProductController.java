/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ProductsControllers;

import dao.ProductDAO;
import dto.Product;
import dto.User;
import dto.Category;
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
@WebServlet(name = "CreateProductController", urlPatterns = {"/CreateProductController"})
public class CreateProductController extends HttpServlet {
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
            ProductDAO dao = new ProductDAO();
            List<Category> categories = dao.getAllCategories();
            request.setAttribute("categories", categories);
            
            String name = request.getParameter("name");
            String categoryIDStr = request.getParameter("categoryID");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String status = request.getParameter("status");
            
            String nameSearch = request.getParameter("nameSearch") != null ? request.getParameter("nameSearch") : "";
            String cateSearch = request.getParameter("cateSearch") != null ? request.getParameter("cateSearch") : "";
            Float priceSearch = request.getParameter("priceSearch") != null ? Float.parseFloat(request.getParameter("priceSearch")) : 0;
            String statusSearch = request.getParameter("statusSearch") != null ? request.getParameter("statusSearch") : "";
            
            if (name == null || name.isEmpty() || categoryIDStr == null || categoryIDStr.isEmpty() || priceStr == null || priceStr.isEmpty() || quantityStr == null || quantityStr.isEmpty() || status == null || status.isEmpty()) {
                request.setAttribute("MSG", "Please enter information!!");
                setProductListAttributes(request, loginUser, nameSearch, cateSearch, priceSearch, statusSearch);
                request.getRequestDispatcher("product/productList.jsp").forward(request, response);
                return;
            }
            int categoryID = Integer.parseInt(categoryIDStr);
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            if (price <= 0 || quantity <= 0) {
                request.setAttribute("MSG", "Price or Quantity must be greater than 0!!");
                setProductListAttributes(request, loginUser, nameSearch, cateSearch, priceSearch, statusSearch);
                request.getRequestDispatcher("product/productList.jsp").forward(request, response);
                return;
            }
            
            String sellerID = loginUser.getUserID();
            if (dao.createProduct(name, categoryID, price, quantity, status, sellerID)) {
                request.setAttribute("MSG", "Product created successfully.");
            } else {
                request.setAttribute("MSG", "Failed to create product.");
            }
            setProductListAttributes(request, loginUser, nameSearch, cateSearch, priceSearch, statusSearch);
            request.getRequestDispatcher("product/productList.jsp").forward(request, response);
        } catch (Exception e) {
            log("Error in CreateProductController: " + e.getMessage(), e);
            setProductListAttributes(request, (User) request.getSession().getAttribute("LOGIN_USER"), "", "", -1, "");
            request.getRequestDispatcher("product/productList.jsp").forward(request, response);
        }
    }

    private void setProductListAttributes(HttpServletRequest request, User loginUser, String nameSearch, String cateSearch, float priceSearch, String statusSearch) {
        try {
            ProductDAO dao = new ProductDAO();
            List<Product> list;
            if ("AD".equals(loginUser.getRoleID())) {
                list = dao.search(nameSearch, cateSearch, priceSearch, statusSearch);
            } else {
                list = dao.getProductsByUser(loginUser.getUserID(), nameSearch, cateSearch, priceSearch, statusSearch);
            }
            request.setAttribute("list", list);
            request.setAttribute("nameSearch", nameSearch);
            request.setAttribute("cateSearch", cateSearch);
            request.setAttribute("priceSearch", priceSearch);
            request.setAttribute("statusSearch", statusSearch);
        } catch (Exception e) {
            log("Error in setProductListAttributes: " + e.getMessage(), e);
            request.setAttribute("list", new ArrayList<Product>());
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
