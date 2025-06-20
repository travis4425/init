package controller;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.ProductDAO;
import dto.Product;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

/**
 * CartController - Xử lý các thao tác với giỏ hàng
 * @author Admin
 */
@WebServlet(name="CartController", urlPatterns={"/CartController"})
public class CartController extends HttpServlet {
    
    private CartDAO cartDAO = new CartDAO();
    private CartDetailDAO cartDetailDAO = new CartDetailDAO();
    private ProductDAO productDAO = new ProductDAO();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");
        
        // Kiểm tra đăng nhập
        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "view";
        
        try {
            switch (action) {
                case "add":
                    addToCart(request, response, loginUser);
                    break;
                case "update":
                    updateCart(request, response, loginUser);
                    break;
                case "remove":
                    removeFromCart(request, response, loginUser);
                    break;
                case "clear":
                    clearCart(request, response, loginUser);
                    break;
                case "view":
                default:
                    viewCart(request, response, loginUser);
                    break;
            }
        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
            request.setAttribute("MSG", "Đã xảy ra lỗi khi xử lý giỏ hàng: " + e.getMessage());
            viewCart(request, response, loginUser);
        }
    }
    
    // Thêm sản phẩm vào giỏ hàng
    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");
        
        if (productIDStr == null || quantityStr == null) {
            request.setAttribute("MSG", "Thông tin sản phẩm không hợp lệ.");
            viewCart(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                request.setAttribute("MSG", "Số lượng phải lớn hơn 0.");
                viewCart(request, response, user);
                return;
            }
            
            // Kiểm tra sản phẩm có tồn tại không
            Product product = productDAO.getProduct(String.valueOf(productID));
            if (product == null) {
                request.setAttribute("MSG", "Sản phẩm không tồn tại.");
                viewCart(request, response, user);
                return;
            }
            
            // Kiểm tra số lượng trong kho
            if (product.getQuantity() < quantity) {
                request.setAttribute("MSG", "Không đủ hàng trong kho. Còn lại: " + product.getQuantity());
                viewCart(request, response, user);
                return;
            }
            
            // Lấy hoặc tạo cart cho user
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            // Thêm vào giỏ hàng
            boolean success = cartDetailDAO.addToCart(cartID, productID, quantity);
            
            if (success) {
                request.setAttribute("MSG", "Đã thêm sản phẩm vào giỏ hàng thành công!");
            } else {
                request.setAttribute("MSG", "Không thể thêm sản phẩm vào giỏ hàng.");
            }
            
            viewCart(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "Dữ liệu không hợp lệ.");
            viewCart(request, response, user);
        }
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ
    private void updateCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");
        
        if (productIDStr == null || quantityStr == null) {
            request.setAttribute("MSG", "Thông tin cập nhật không hợp lệ.");
            viewCart(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 0) {
                request.setAttribute("MSG", "Số lượng không thể âm.");
                viewCart(request, response, user);
                return;
            }
            
            // Lấy cart hiện tại
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            boolean success;
            if (quantity == 0) {
                // Xóa sản phẩm nếu quantity = 0
                success = cartDetailDAO.removeFromCart(cartID, productID);
                request.setAttribute("MSG", success ? "Đã xóa sản phẩm khỏi giỏ hàng." : "Không thể xóa sản phẩm.");
            } else {
                // Kiểm tra số lượng trong kho
                Product product = productDAO.getProduct(String.valueOf(productID));
                if (product != null && product.getQuantity() < quantity) {
                    request.setAttribute("MSG", "Không đủ hàng trong kho. Còn lại: " + product.getQuantity());
                    viewCart(request, response, user);
                    return;
                }
                
                success = cartDetailDAO.setQuantity(cartID, productID, quantity);
                request.setAttribute("MSG", success ? "Đã cập nhật giỏ hàng." : "Không thể cập nhật giỏ hàng.");
            }
            
            viewCart(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "Dữ liệu không hợp lệ.");
            viewCart(request, response, user);
        }
    }
    
    // Xóa sản phẩm khỏi giỏ hàng
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        
        if (productIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy sản phẩm cần xóa.");
            viewCart(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            boolean success = cartDetailDAO.removeFromCart(cartID, productID);
            
            if (success) {
                request.setAttribute("MSG", "Đã xóa sản phẩm khỏi giỏ hàng.");
            } else {
                request.setAttribute("MSG", "Không thể xóa sản phẩm.");
            }
            
            viewCart(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID sản phẩm không hợp lệ.");
            viewCart(request, response, user);
        }
    }
    
    // Xóa toàn bộ giỏ hàng
    private void clearCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        int cartID = cartDAO.getCurrentCartID(user.getUserID());
        boolean success = cartDetailDAO.clearCart(cartID);
        
        if (success) {
            request.setAttribute("MSG", "Đã xóa toàn bộ giỏ hàng.");
        } else {
            request.setAttribute("MSG", "Không thể xóa giỏ hàng.");
        }
        
        viewCart(request, response, user);
    }
    
    // Hiển thị giỏ hàng
    private void viewCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        int cartID = cartDAO.getCurrentCartID(user.getUserID());
        
        // Lấy danh sách sản phẩm trong giỏ
        Map<Product, Integer> cartItems = cartDetailDAO.getCartWithProducts(cartID);
        
        // Tính tổng tiền
        float totalAmount = cartDetailDAO.getTotalAmount(cartID);
        
        // Đếm số lượng items
        int itemCount = cartDetailDAO.getCartItemCount(cartID);
        
        // Set attributes
        request.setAttribute("CART_ITEMS", cartItems);
        request.setAttribute("TOTAL_AMOUNT", totalAmount);
        request.setAttribute("ITEM_COUNT", itemCount);
        request.setAttribute("CART_ID", cartID);
        
        // Forward to JSP
        request.getRequestDispatcher("viewCart.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        return "Cart Controller";
    }
    // </editor-fold>
}