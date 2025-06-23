package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String LOGIN = "Login";
    private static final String LOGIN_CONTROLLER = "LoginController";
    private static final String REGISTER = "Register";
    private static final String REGISTER_CONTROLLER = "RegisterController";

    private static final String SEARCH_USER = "SearchUser";
    private static final String SEARCH_USER_CONTROLLER = "SearchUserController";
    private static final String CREATE_USER = "CreateUser";
    private static final String CREATE_USER_CONTROLLER = "CreateUserController";
    private static final String UPDATE_USER = "UpdateUser";
    private static final String UPDATE_USER_CONTROLLER = "UpdateUserController";
    private static final String DELETE_USER = "DeleteUser";
    private static final String DELETE_USER_CONTROLLER = "DeleteUserController";
    private static final String GET_USER = "GetUser";
    private static final String GET_USER_CONTROLLER = "GetUserController";

    private static final String SEARCH_CART = "ViewCart";
    private static final String SEARCH_CART_CONTROLLER = "SearchCartController";
    private static final String CREATE_CART = "CreateCart";
    private static final String CREATE_CART_CONTROLLER = "CreateCartController";
    private static final String DELETE_CART = "DeleteCart";
    private static final String DELETE_CART_CONTROLLER = "DeleteCartController";
    private static final String UPDATE_CART = "UpdateCart";
    private static final String UPDATE_CART_CONTROLLER = "UpdateCartController";

    private static final String SEARCH_INVOICE = "ViewInvoice";
    private static final String SEARCH_INVOICE_CONTROLLER = "SearchInvoiceController";
    private static final String CREATE_INVOICE = "CreateInvoice";
    private static final String CREATE_INVOICE_CONTROLLER = "CreateInvoiceController";
    private static final String DELETE_INVOICE = "DeleteInvoice";
    private static final String DELETE_INVOICE_CONTROLLER = "DeleteInvoiceController";
    private static final String UPDATE_INVOICE = "UpdateInvoice";
    private static final String UPDATE_INVOICE_CONTROLLER = "UpdateInvoiceController";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "error.jsp";
        try {
            String action = request.getParameter("action");
            if (LOGIN.equals(action)) {
                url = LOGIN_CONTROLLER;
            } else if (REGISTER.equals(action)) {
                url = REGISTER_CONTROLLER;
            } else if (SEARCH_USER.equals(action)) {
                url = SEARCH_USER_CONTROLLER;
            } else if (CREATE_USER.equals(action)) {
                url = CREATE_USER_CONTROLLER;
            } else if (GET_USER.equals(action)) {
                url = GET_USER_CONTROLLER;
            } else if (UPDATE_USER.equals(action)) {
                url = UPDATE_USER_CONTROLLER;
            } else if (DELETE_USER.equals(action)) {
                url = DELETE_USER_CONTROLLER;
            } else if (SEARCH_CART.equals(action)) {
                url = SEARCH_CART_CONTROLLER;
            } else if (CREATE_CART.equals(action)) {
                url = CREATE_CART_CONTROLLER;
            } else if (DELETE_CART.equals(action)) {
                url = DELETE_CART_CONTROLLER;
            } else if (UPDATE_CART.equals(action)) {
                url = UPDATE_CART_CONTROLLER;
            }
            else if (SEARCH_INVOICE.equals(action)) {
                url = SEARCH_INVOICE_CONTROLLER;
            } else if (CREATE_INVOICE.equals(action)) {
                url = CREATE_INVOICE_CONTROLLER;
            } else if (DELETE_INVOICE.equals(action)) {
                url = DELETE_INVOICE_CONTROLLER;
            } else if (UPDATE_INVOICE.equals(action)) {
                url = UPDATE_INVOICE_CONTROLLER;
            } else {
                url = "login.jsp";
            }
        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST cũng xử lý giống GET
        doGet(request, response);
    }
}
