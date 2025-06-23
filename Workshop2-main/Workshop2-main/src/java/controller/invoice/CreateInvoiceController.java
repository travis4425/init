package controller.invoice;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.ProductDAO;
import dto.Product;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "CheckoutController", urlPatterns = {"/CheckoutController"})
public class CreateInvoiceController extends HttpServlet {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();
    private final CartDAO cartDAO = new CartDAO();
    private final CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private final ProductDAO productDAO = new ProductDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int cartID = cartDAO.getCurrentCartID(loginUser.getUserID());
            Map<Product, Integer> cartItems = cartDetailDAO.getCartWithProducts(cartID);
            if (cartItems.isEmpty()) {
                request.setAttribute("MSG", "Giỏ hàng trống, không thể thanh toán.");
                request.getRequestDispatcher("cartList.jsp").forward(request, response);
                return;
            }

            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                if (!productDAO.checkProductAvailability(entry.getKey().getProductID(), entry.getValue())) {
                    request.setAttribute("MSG", "Sản phẩm " + entry.getKey().getName() + " không đủ số lượng trong kho.");
                    request.getRequestDispatcher("cartList.jsp").forward(request, response);
                    return;
                }
            }

            float totalAmount = cartDetailDAO.getTotalAmount(cartID);
            int invoiceID = invoiceDAO.createInvoice(loginUser.getUserID(), totalAmount);
            if (invoiceID > 0 && invoiceDetailDAO.createInvoiceDetailsFromCart(invoiceID, cartID)) {
                for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                    productDAO.updateProductQuantity(entry.getKey().getProductID(), entry.getValue());
                }
                cartDetailDAO.clearCart(cartID);
                request.setAttribute("INVOICE_ID", invoiceID);
                request.setAttribute("TOTAL_AMOUNT", totalAmount);
                request.getRequestDispatcher("thankyou.jsp").forward(request, response);
            } else {
                request.setAttribute("MSG", "Thanh toán thất bại.");
                request.getRequestDispatcher("cartList.jsp").forward(request, response);
            }
        } catch (Exception e) {
            log("Error at CheckoutController: " + e.getMessage());
            request.setAttribute("MSG", "Thanh toán thất bại: " + e.getMessage());
            request.getRequestDispatcher("cartList.jsp").forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "CreateInvoiceController handles checkout";
    }
}