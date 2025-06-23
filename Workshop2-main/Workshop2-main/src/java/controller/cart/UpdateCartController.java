package controller.Cart;

import dto.User;
import dto.Cart;
import dao.CartDAO;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author ACER
 */
@WebServlet(name = "UpdateCartController", urlPatterns = {"/UpdateCartController"})
public class UpdateCartController extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String CART_LIST_PAGE = "UpdateCartController";
    private static final String UPDATE_CART_PAGE = "updateCart.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = CART_LIST_PAGE;

        try {
            User loginUser = (User) request.getSession().getAttribute("LOGIN_USER");
            if (loginUser == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            String id = request.getParameter("id");
            CartDAO dao = new CartDAO();
            Cart cart = dao.getCartById(id);

            if (cart == null) {
                request.setAttribute("MSG", "Cannot find the cart!!");
                request.getRequestDispatcher(url).forward(request, response);
                return;
            }
           
            String userID = request.getParameter("userID");
            String createdDate = request.getParameter("createdDate");
            if (userID == null && createdDate == null) {
                request.setAttribute("MSG", "The input cannot be blank!!");
                request.setAttribute("CART", cart);
                request.getRequestDispatcher(UPDATE_CART_PAGE).forward(request, response);
                return;
            }
            
            cart.setUserID(userID);
            cart.setCreatedDate(createdDate);
            boolean updated = dao.updateCart(userID, createdDate);
            if (updated) {
                request.setAttribute("MSG", "Update Successfully!!");
                url = CART_LIST_PAGE;
            } else {
                request.setAttribute("MSG", "Update Failed!!");
                request.setAttribute("CART", cart);
                url = UPDATE_CART_PAGE;
            }
            request.getRequestDispatcher(url).forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("MSG", "Database error!!");
            request.getRequestDispatcher(CART_LIST_PAGE).forward(request, response);
        } catch (Exception e) {
            log("Error at UpdateCartController: " + e.toString());
            request.setAttribute("MSG", "System error: " + e.getMessage());
            request.getRequestDispatcher(CART_LIST_PAGE).forward(request, response);
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