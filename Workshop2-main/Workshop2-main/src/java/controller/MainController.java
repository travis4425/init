/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

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
@WebServlet(name="MainController", urlPatterns={"/MainController"})
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
    
    // Cart & Invoice actions
    private static final String ADD_TO_CART = "AddToCart";
    private static final String VIEW_CART = "ViewCart";
    private static final String UPDATE_CART = "UpdateCart";
    private static final String REMOVE_FROM_CART = "RemoveFromCart";
    private static final String CLEAR_CART = "ClearCart";
    private static final String CART_CONTROLLER = "CartController";
    
    private static final String CHECKOUT = "Checkout";
    private static final String VIEW_INVOICES = "ViewInvoices";
    private static final String VIEW_INVOICE_DETAIL = "ViewInvoiceDetail";
    private static final String INVOICE_CONTROLLER = "InvoiceController";
    
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
            }
            // Cart actions
            else if (ADD_TO_CART.equals(action) || VIEW_CART.equals(action) || 
                     UPDATE_CART.equals(action) || REMOVE_FROM_CART.equals(action) || 
                     CLEAR_CART.equals(action)) {
                url = CART_CONTROLLER;
            }
            // Invoice actions
            else if (CHECKOUT.equals(action) || VIEW_INVOICES.equals(action) || 
                     VIEW_INVOICE_DETAIL.equals(action)) {
                url = INVOICE_CONTROLLER;
            }
            else {
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