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
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ACER
 */
@WebServlet(name = "UpdateProductController", urlPatterns = {"/UpdateProductController"})
public class UpdateProductController extends HttpServlet {
    private static final String PRODUCT_LIST_PAGE = "SearchProductController";
    private static final String UPDATE_PRODUCT_PAGE = "product/updateProduct.jsp"; // Sửa thành updateProduct.jsp

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int id = Integer.parseInt(request.getParameter("id"));
            ProductDAO dao = new ProductDAO();
            Product product = dao.getProductById(id);

            if (product == null) {
                request.setAttribute("MSG", "Cannot find the product!!");
                request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
                return;
            }

            List<Category> categories = dao.getAllCategories();
            request.setAttribute("categories", categories);
            request.setAttribute("PRODUCT", product);
            request.getRequestDispatcher(UPDATE_PRODUCT_PAGE).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("MSG", "Database error!!");
            request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
        } catch (Exception e) {
            log("Error at UpdateProductController: " + e.toString());
            request.setAttribute("MSG", "System error: " + e.getMessage());
            request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = PRODUCT_LIST_PAGE;
        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            int id = Integer.parseInt(request.getParameter("id"));
            ProductDAO dao = new ProductDAO();
            Product product = dao.getProductById(id);

            if (product == null) {
                request.setAttribute("MSG", "Cannot find the product!!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }

            String name = request.getParameter("name");
            String categoryIDStr = request.getParameter("categoryID");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String status = request.getParameter("status");

            if (name == null || name.isEmpty() || categoryIDStr == null || categoryIDStr.isEmpty() || 
                priceStr == null || priceStr.isEmpty() || quantityStr == null || quantityStr.isEmpty() || 
                status == null || status.isEmpty()) {
                request.setAttribute("MSG", "The input cannot be blank!!");
                List<Category> categories = dao.getAllCategories();
                request.setAttribute("categories", categories);
                request.setAttribute("PRODUCT", product);
                request.getRequestDispatcher(UPDATE_PRODUCT_PAGE).forward(request, response);
                return;
            }

            int categoryID = Integer.parseInt(categoryIDStr);
            float price = Float.parseFloat(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if (price <= 0 || quantity <= 0) {
                request.setAttribute("MSG", "Price or Quantity must be greater than 0!!");
                List<Category> categories = dao.getAllCategories();
                request.setAttribute("categories", categories);
                request.setAttribute("PRODUCT", product);
                request.getRequestDispatcher(UPDATE_PRODUCT_PAGE).forward(request, response);
                return;
            }

            if (!dao.categoryExists(categoryID)) {
                request.setAttribute("MSG", "Selected category does not exist!");
                List<Category> categories = dao.getAllCategories();
                request.setAttribute("categories", categories);
                request.setAttribute("PRODUCT", product);
                request.getRequestDispatcher(UPDATE_PRODUCT_PAGE).forward(request, response);
                return;
            }
            
            if (!dao.canUpdate(name, categoryID, loginUser.getUserID())) {
                request.setAttribute("MSG", "Duplicated product!!");
                List<Category> categories = dao.getAllCategories();
                request.setAttribute("categories", categories);
                request.setAttribute("PRODUCT", product);
                request.getRequestDispatcher(UPDATE_PRODUCT_PAGE).forward(request, response);
                return;
            }

            product.setName(name);
            product.setCategoryID(categoryID);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setStatus(status);
            boolean updated = dao.updateProduct(id, name, categoryID, price, quantity, status);
            if (updated) {
                request.setAttribute("MSG", "Update Successfully!!");
                request.setAttribute("nameSearch", request.getParameter("nameSearch"));
                request.setAttribute("cateSearch", request.getParameter("cateSearch"));
                request.setAttribute("priceSearch", request.getParameter("priceSearch"));
                request.setAttribute("statusSearch", request.getParameter("statusSearch"));
                url = PRODUCT_LIST_PAGE;
            } else {
                request.setAttribute("MSG", "Update Failed!!");
                request.setAttribute("PRODUCT", product);
                url = PRODUCT_LIST_PAGE;
            }
            request.getRequestDispatcher(url).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("MSG", "Database error!!");
            request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
        } catch (Exception e) {
            log("Error at UpdateProductController: " + e.toString());
            request.setAttribute("MSG", "System error: " + e.getMessage());
            request.getRequestDispatcher(PRODUCT_LIST_PAGE).forward(request, response);
        }
    }
}
