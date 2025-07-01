package controller.inventory;

import dao.InventoryDAO;
import dto.Inventory;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchInventoryController", urlPatterns = {"/SearchInventoryController"})
public class SearchInventoryController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null || (!"AD".equals(loginUser.getRoleID()) && !"SE".equals(loginUser.getRoleID()))) {
            response.sendRedirect("login.jsp");
            return;
        }
        try {
            Integer wSearch = null;
            Integer pSearch = null;
            if (request.getParameter("warehouseSearch") != null && !request.getParameter("warehouseSearch").isEmpty()) {
                wSearch = Integer.parseInt(request.getParameter("warehouseSearch"));
            }
            if (request.getParameter("productSearch") != null && !request.getParameter("productSearch").isEmpty()) {
                pSearch = Integer.parseInt(request.getParameter("productSearch"));
            }
            InventoryDAO dao = new InventoryDAO();
            List<Inventory> list = dao.searchInventories(wSearch, pSearch);
            request.setAttribute("LIST", list);
            request.setAttribute("warehouseSearch", wSearch);
            request.setAttribute("productSearch", pSearch);
        } catch (Exception e) {
            log("Error at SearchInventoryController: " + e.toString());
            request.setAttribute("MSG", "Error loading inventory list: " + e.getMessage());
        }
        request.getRequestDispatcher("inventory/inventoryList.jsp").forward(request, response);
   System.out.println("✅ SearchInventoryController called, forwarding to JSP");

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