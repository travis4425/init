package controller.inventory;

import dao.InventoryDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "UpdateInventoryController", urlPatterns = {"/UpdateInventoryController"})
public class UpdateInventoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (!"AD".equals(loginUser.getRoleID()) && !"SE".equals(loginUser.getRoleID()))) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            int warehouseID = Integer.parseInt(request.getParameter("warehouseID"));
            int productID = Integer.parseInt(request.getParameter("productID"));
            int quantity = Integer.parseInt(request.getParameter("stockQuantity"));
            InventoryDAO dao = new InventoryDAO();
            boolean result = dao.updateInventory(warehouseID, productID, quantity);
            request.setAttribute("MSG", result ? "Inventory updated successfully." : "Failed to update inventory.");
        } catch (Exception e) {
            log("Error at UpdateInventoryController: " + e.toString());
            request.setAttribute("MSG", "Operation failed: " + e.getMessage());
        }
        request.getRequestDispatcher("SearchInventoryController").forward(request, response);
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