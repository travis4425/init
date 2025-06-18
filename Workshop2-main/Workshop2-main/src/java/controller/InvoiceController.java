package controller;

import dao.CartDAO;
import dao.InvoiceDAO;
import dto.Cart;
import dto.Invoice;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

/**
 * Invoice Controller - Handle invoice operations
 * @author Admin
 */
@WebServlet(name="InvoiceController", urlPatterns={"/InvoiceController"})
public class InvoiceController extends HttpServlet {
    
    private static final String CHECKOUT = "Checkout";
    private static final String VIEW_INVOICES = "ViewInvoices";
    private static final String VIEW_INVOICE_DETAIL = "ViewInvoiceDetail";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "login.jsp";
        try {
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("LOGIN_USER");
            
            if (loginUser == null) {
                request.setAttribute("MSG", "Please login to access this feature.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }
            
            String action = request.getParameter("action");
            InvoiceDAO invoiceDAO = new InvoiceDAO();
            CartDAO cartDAO = new CartDAO();
            
            if (CHECKOUT.equals(action)) {
                url = handleCheckout(request, invoiceDAO, cartDAO, loginUser.getUserID());
            } else if (VIEW_INVOICES.equals(action)) {
                url = handleViewInvoices(request, invoiceDAO, loginUser.getUserID());
            } else if (VIEW_INVOICE_DETAIL.equals(action)) {
                url = handleViewInvoiceDetail(request, invoiceDAO);
            } else {
                url = handleViewInvoices(request, invoiceDAO, loginUser.getUserID());
            }
            
        } catch (Exception e) {
            log("Error at InvoiceController: " + e.toString());
            request.setAttribute("MSG", "An error occurred while processing your request.");
            url = "error.jsp";
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
    
    private String handleCheckout(HttpServletRequest request, InvoiceDAO invoiceDAO, CartDAO cartDAO, String userID) {
        try {
            // Get cart items
            List<Cart> cartList = cartDAO.getCartByID(userID);
            
            if (cartList.isEmpty()) {
                request.setAttribute("MSG", "Your cart is empty. Please add some products first.");
                return "CartController?action=ViewCart";
            }
            
            // Calculate total amount
            double totalAmount = cartDAO.getCartTotal(userID);
            
            // Generate invoice ID
            String invoiceID = invoiceDAO.generateInvoiceID();
            
            // Create invoice
            Date currentDate = new Date(System.currentTimeMillis());
            InvoiceDTO invoice = new InvoiceDTO(invoiceID, userID, currentDate, totalAmount, "COMPLETED");
            
            // Save invoice and details
            boolean success = invoiceDAO.createInvoice(invoice, cartList);
            
            if (success) {
                // Clear cart after successful checkout
                cartDAO.clearCart(userID);
                
                // Set attributes for thank you page
                request.setAttribute("invoice", invoice);
                request.setAttribute("invoiceDetails", cartList);
                request.setAttribute("MSG", "Order placed successfully! Invoice ID: " + invoiceID);
                
                return "thankyou.jsp";
            } else {
                request.setAttribute("MSG", "Failed to process your order. Please try again.");
                return "CartController?action=ViewCart";
            }
            
        } catch (Exception e) {
            log("Error during checkout: " + e.toString());
            request.setAttribute("MSG", "Error processing your order. Please try again.");
            return "CartController?action=ViewCart";
        }
    }
    
    private String handleViewInvoices(HttpServletRequest request, InvoiceDAO invoiceDAO, String userID) {
        try {
            List<InvoiceDTO> invoiceList = invoiceDAO.getInvoicesByUser(userID);
            request.setAttribute("invoiceList", invoiceList);
            return "invoiceList.jsp";
            
        } catch (Exception e) {
            log("Error viewing invoices: " + e.toString());
            request.setAttribute("MSG", "Error loading invoices.");
            return "error.jsp";
        }
    }
    
    private String handleViewInvoiceDetail(HttpServletRequest request, InvoiceDAO invoiceDAO) {
        try {
            String invoiceID = request.getParameter("invoiceID");
            
            if (invoiceID == null) {
                request.setAttribute("MSG", "Invoice ID is required.");
                return "error.jsp";
            }
            
            InvoiceDTO invoice = invoiceDAO.getInvoiceByID(invoiceID);
            
            if (invoice == null) {
                request.setAttribute("MSG", "Invoice not found.");
                return "error.jsp";
            }
            
            request.setAttribute("invoice", invoice);
            return "invoiceDetail.jsp";
            
        } catch (Exception e) {
            log("Error viewing invoice detail: " + e.toString());
            request.setAttribute("MSG", "Error loading invoice details.");
            return "error.jsp";
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