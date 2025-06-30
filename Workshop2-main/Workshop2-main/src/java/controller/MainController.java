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
@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    // login, register
    private static final String LOGIN = "Login";
    private static final String LOGIN_CONTROLLER = "LoginController";
    private static final String REGISTER = "Register";
    private static final String REGISTER_CONTROLLER = "RegisterController";

    // CRUD for User
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

    // category
    private static final String SEARCH_CATEGORY = "ViewCategories";
    private static final String SEARCH_CATEGORY_CONTROLLER = "SearchCategoryController";
    private static final String CREATE_CATEGORY = "CreateCategory";
    private static final String CREATE_CATEGORY_CONTROLLER = "CreateCategoryController";
    private static final String DELETE_CATEGORY = "DeleteCategory";
    private static final String DELETE_CATEGORY_CONTROLLER = "DeleteCategoryController";
    private static final String UPDATE_CATEGORY = "UpdateCategory";
    private static final String UPDATE_CATEGORY_CONTROLLER = "UpdateCategoryController";

    // product
    private static final String SEARCH_PRODUCT = "ViewProducts";
    private static final String SEARCH_PRODUCT_CONTROLLER = "SearchProductController";
    private static final String CREATE_PRODUCT = "CreateProduct";
    private static final String CREATE_PRODUCT_CONTROLLER = "CreateProductController";
    private static final String DELETE_PRODUCT = "DeleteProduct";
    private static final String DELETE_PRODUCT_CONTROLLER = "DeleteProductController";
    private static final String UPDATE_PRODUCT = "UpdateProduct";
    private static final String UPDATE_PRODUCT_CONTROLLER = "UpdateProductController";

    // customer care
    private static final String CUSTOMER_CARE = "CustomerCare";
    private static final String CUSTOMER_CARE_CONTROLLER = "CustomerCareController";
    private static final String SEARCH_CUSTOMER_CARE = "SearchCustomerCare";
    private static final String SEARCH_CUSTOMER_CARE_CONTROLLER = "SearchCustomerCareController";
    private static final String CREATE_CUSTOMER_CARE = "CreateCustomerCare";
    private static final String CREATE_CUSTOMER_CARE_CONTROLLER = "CreateCustomerCareController";
    private static final String REPLY_CUSTOMER_CARE = "ReplyCustomerCare";
    private static final String REPLY_CUSTOMER_CARE_CONTROLLER = "ReplyCustomerCareController";
    private static final String DELETE_CUSTOMER_CARE = "DeleteCustomerCare";
    private static final String DELETE_CUSTOMER_CARE_CONTROLLER = "DeleteCustomerCareController";

    // promotion
    private static final String PROMOTION = "Promotion";
    private static final String PROMOTION_CONTROLLER = "PromotionController";
    private static final String APPLY_PROMOTION = "ApplyPromotion";
    private static final String APPLY_PROMOTION_CONTROLLER = "ApplyPromotionController";
    private static final String APPLY_CATEGORY_PROMOTION = "ApplyCategoryPromotion";
    private static final String APPLY_CATEGORY_PROMOTION_CONTROLLER = "ApplyCategoryPromotionController";

    // cart
    private static final String ADD_TO_CART = "AddToCart";
    private static final String ADD_TO_CART_CONTROLLER = "AddToCartController";
    private static final String VIEW_CART = "ViewCart";
    private static final String VIEW_CART_CONTROLLER = "ViewCartController";
    private static final String DELETE_FROM_CART = "DeleteFromCart";
    private static final String DELETE_FROM_CART_CONTROLLER = "DeleteFromCartController";
    private static final String SEARCH_CART = "SearchCart";
    private static final String SEARCH_CART_CONTROLLER = "SearchCartController";

    // pay
    private static final String CHECKOUT = "Checkout";
    private static final String CHECKOUT_CONTROLLER = "CheckoutController";

    // invoice
    private static final String VIEW_INVOICE = "ViewInvoice";
    private static final String VIEW_INVOICE_CONTROLLER = "ViewInvoiceController";
    private static final String CANCEL_INVOICE = "CancelInvoice";
    private static final String CANCEL_INVOICE_CONTROLLER = "CancelInvoiceController";
    private static final String SEARCH_INVOICE = "SearchInvoice";
    private static final String SEARCH_INVOICE_CONTROLLER = "SearchInvoiceController";

    // return
    private static final String VIEW_RETURN = "ViewReturn";
    private static final String VIEW_RETURN_CONTROLLER = "ViewReturnController";
    private static final String RETURN_REQUEST = "ReturnRequest";
    private static final String RETURN_REQUEST_CONTROLLER = "ReturnRequestController";
    private static final String UPDATE_STATUS_RETURN = "UpdateStatusReturn";
    private static final String UPDATE_STATUS_RETURN_CONTROLLER = "UpdateStatusReturnController";

    // delivery
    private static final String VIEW_DELIVERY = "ViewDelivery";
    private static final String VIEW_DELIVERY_CONTROLLER = "ViewDeliveryController";
    private static final String UPDATE_STATUS_DELIVERY = "UpdateStatusDelivery";
    private static final String UPDATE_STATUS_DELIVERY_CONTROLLER = "UpdateStatusDeliveryController";
    private static final String SEARCH_DELIVERY = "SearchDelivery";
    private static final String SEARCH_DELIVERY_CONTROLLER = "SearchDeliveryController";

    // inventory
    private static final String INVENTORY = "Inventory";
    private static final String INVENTORY_CONTROLLER = "SearchInventoryController";
    private static final String CREATE_INVENTORY = "CreateInventory";
    private static final String CREATE_INVENTORY_CONTROLLER = "CreateInventoryController";
    private static final String UPDATE_INVENTORY = "UpdateInventory";
    private static final String UPDATE_INVENTORY_CONTROLLER = "UpdateInventoryController";
    private static final String DELETE_INVENTORY = "DeleteInventory";
    private static final String DELETE_INVENTORY_CONTROLLER = "DeleteInventoryController";

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
            } else if (SEARCH_CATEGORY.equals(action)) {
                url = SEARCH_CATEGORY_CONTROLLER;
            } else if (CREATE_CATEGORY.equals(action)) {
                url = CREATE_CATEGORY_CONTROLLER;
            } else if (DELETE_CATEGORY.equals(action)) {
                url = DELETE_CATEGORY_CONTROLLER;
            } else if (UPDATE_CATEGORY.equals(action)) {
                url = UPDATE_CATEGORY_CONTROLLER;
            } else if (SEARCH_PRODUCT.equals(action)) {
                url = SEARCH_PRODUCT_CONTROLLER;
            } else if (CREATE_PRODUCT.equals(action)) {
                url = CREATE_PRODUCT_CONTROLLER;
            } else if (DELETE_PRODUCT.equals(action)) {
                url = DELETE_PRODUCT_CONTROLLER;
            } else if (UPDATE_PRODUCT.equals(action)) {
                url = UPDATE_PRODUCT_CONTROLLER;
            } else if (CUSTOMER_CARE.equals(action)) {
                url = CUSTOMER_CARE_CONTROLLER;
            } else if (SEARCH_CUSTOMER_CARE.equals(action)) {
                url = SEARCH_CUSTOMER_CARE_CONTROLLER;
            } else if (CREATE_CUSTOMER_CARE.equals(action)) {
                url = CREATE_CUSTOMER_CARE_CONTROLLER;
            } else if (REPLY_CUSTOMER_CARE.equals(action)) {
                url = REPLY_CUSTOMER_CARE_CONTROLLER;
            } else if (DELETE_CUSTOMER_CARE.equals(action)) {
                url = DELETE_CUSTOMER_CARE_CONTROLLER;
            } else if (PROMOTION.equals(action)) {
                url = PROMOTION_CONTROLLER;
            } else if (APPLY_PROMOTION.equals(action)) {
                url = APPLY_PROMOTION_CONTROLLER;
            } else if (APPLY_CATEGORY_PROMOTION.equals(action)) {
                url = APPLY_CATEGORY_PROMOTION_CONTROLLER;
            } else if (ADD_TO_CART.equals(action)) {
                url = ADD_TO_CART_CONTROLLER;
            } else if (VIEW_CART.equals(action)) {
                url = VIEW_CART_CONTROLLER;
            } else if (DELETE_FROM_CART.equals(action)) {
                url = DELETE_FROM_CART_CONTROLLER;
            } else if (SEARCH_CART.equals(action)) {
                url = SEARCH_CART_CONTROLLER;
            } else if (CHECKOUT.equals(action)) {
                url = CHECKOUT_CONTROLLER;
            } else if (VIEW_INVOICE.equals(action)) {
                url = VIEW_INVOICE_CONTROLLER;
            } else if (CANCEL_INVOICE.equals(action)) {
                url = CANCEL_INVOICE_CONTROLLER;
            } else if (VIEW_RETURN.equals(action)) {
                url = VIEW_RETURN_CONTROLLER;
            } else if (VIEW_DELIVERY.equals(action)) {
                url = VIEW_DELIVERY_CONTROLLER;
            } else if (UPDATE_STATUS_DELIVERY.equals(action)) {
                url = UPDATE_STATUS_DELIVERY_CONTROLLER;
            } else if (RETURN_REQUEST.equals(action)) {
                url = RETURN_REQUEST_CONTROLLER;
            } else if (UPDATE_STATUS_RETURN.equals(action)) {
                url = UPDATE_STATUS_RETURN_CONTROLLER;
            } else if (SEARCH_INVOICE.equals(action)) {
                url = SEARCH_INVOICE_CONTROLLER;
            } else if (SEARCH_DELIVERY.equals(action)) {
                url = SEARCH_DELIVERY_CONTROLLER;
            } else if (INVENTORY.equals(action)) {
                url = "inventory.jsp";
            } else if (CREATE_INVENTORY.equals(action)) {
                url = CREATE_INVENTORY_CONTROLLER;
            } else if (UPDATE_INVENTORY.equals(action)) {
                url = UPDATE_INVENTORY_CONTROLLER;
            } else if (DELETE_INVENTORY.equals(action)) {
                url = DELETE_INVENTORY_CONTROLLER;
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
