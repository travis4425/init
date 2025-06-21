package controller.cart;

import dao.CartDAO;
import dao.CartDetailDAO;
import dao.ProductDAO;
import dto.Product;
import dto.User;
import dto.Cart;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
        if (action == null) action = "ViewCart";
        
        try {
            switch (action) {
                case "AddToCart":
                    addToCart(request, response, loginUser);
                    break;
                case "UpdateCart":
                    updateCart(request, response, loginUser);
                    break;
                case "RemoveFromCart":
                    removeFromCart(request, response, loginUser);
                    break;
                case "ClearCart":
                    clearCart(request, response, loginUser);
                    break;
                case "ViewCart":
                default:
                    cartList(request, response, loginUser);
                    break;
            }
        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
            e.printStackTrace();
            request.setAttribute("MSG", "Đã xảy ra lỗi khi xử lý giỏ hàng: " + e.getMessage());
            try {
                cartList(request, response, loginUser);
            } catch (Exception ex) {
                response.sendRedirect("MainController");
            }
        }
    }
    
    private void addToCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");
        
        if (productIDStr == null || quantityStr == null) {
            request.setAttribute("MSG", "Thông tin sản phẩm không hợp lệ.");
            cartList(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity <= 0) {
                request.setAttribute("MSG", "Số lượng phải lớn hơn 0.");
                cartList(request, response, user);
                return;
            }
            
            // FIX: Kiểm tra stock trước khi thêm vào cart
            Product product = productDAO.getProduct(String.valueOf(productID));
            if (product == null) {
                request.setAttribute("MSG", "Sản phẩm không tồn tại.");
                cartList(request, response, user);
                return;
            }
            
            // Kiểm tra số lượng hiện tại trong cart
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            int currentQuantityInCart = cartDetailDAO.getProductQuantityInCart(cartID, productID);
            int totalQuantityNeeded = currentQuantityInCart + quantity;
            
            if (product.getQuantity() < totalQuantityNeeded) {
                request.setAttribute("MSG", "Không đủ hàng trong kho. Còn lại: " + 
                    product.getQuantity() + ", bạn đã có " + currentQuantityInCart + " trong giỏ");
                cartList(request, response, user);
                return;
            }
            
            boolean success = cartDetailDAO.addToCart(cartID, productID, quantity);
            
            if (success) {
                request.setAttribute("MSG", "Đã thêm sản phẩm vào giỏ hàng thành công!");
            } else {
                request.setAttribute("MSG", "Không thể thêm sản phẩm vào giỏ hàng.");
            }
            
            cartList(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "Dữ liệu không hợp lệ.");
            cartList(request, response, user);
        }
    }
    
    private void updateCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        String quantityStr = request.getParameter("quantity");
        
        if (productIDStr == null || quantityStr == null) {
            request.setAttribute("MSG", "Thông tin cập nhật không hợp lệ.");
            cartList(request, response, user);
            return;
        }
        
        try {
            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);
            
            if (quantity < 0) {
                request.setAttribute("MSG", "Số lượng không thể âm.");
                cartList(request, response, user);
                return;
            }
            
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            boolean success;
            if (quantity == 0) {
                success = cartDetailDAO.removeFromCart(cartID, productID);
                request.setAttribute("MSG", success ? "Đã xóa sản phẩm khỏi giỏ hàng." : "Không thể xóa sản phẩm.");
            } else {
                Product product = productDAO.getProduct(String.valueOf(productID));
                if (product != null && product.getQuantity() < quantity) {
                    request.setAttribute("MSG", "Không đủ hàng trong kho. Còn lại: " + product.getQuantity());
                    cartList(request, response, user);
                    return;
                }
                
                success = cartDetailDAO.setQuantity(cartID, productID, quantity);
                request.setAttribute("MSG", success ? "Đã cập nhật giỏ hàng." : "Không thể cập nhật giỏ hàng.");
            }
            
            cartList(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "Dữ liệu không hợp lệ.");
            cartList(request, response, user);
        }
    }
    
    private void removeFromCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        String productIDStr = request.getParameter("productID");
        
        if (productIDStr == null) {
            request.setAttribute("MSG", "Không tìm thấy sản phẩm cần xóa.");
            cartList(request, response, user);
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
            
            cartList(request, response, user);
            
        } catch (NumberFormatException e) {
            request.setAttribute("MSG", "ID sản phẩm không hợp lệ.");
            cartList(request, response, user);
        }
    }
    
    private void clearCart(HttpServletRequest request, HttpServletResponse response, User user) 
            throws Exception {
        
        int cartID = cartDAO.getCurrentCartID(user.getUserID());
        boolean success = cartDetailDAO.clearCart(cartID);
        
        if (success) {
            request.setAttribute("MSG", "Đã xóa toàn bộ giỏ hàng.");
        } else {
            request.setAttribute("MSG", "Không thể xóa giỏ hàng.");
        }
        
        cartList(request, response, user);
    }
    
    // FIX: Sửa lỗi productID type conversion
    private void cartList(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        
        try {
            int cartID = cartDAO.getCurrentCartID(user.getUserID());
            
            // Lấy danh sách sản phẩm và convert thành List<CartItem>
            Map<Product, Integer> cartItems = cartDetailDAO.getCartWithProducts(cartID);
            List<Cart> cartList = new ArrayList<>();
            float totalAmount = 0;
            
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                float itemTotal = product.getPrice() * quantity;
                totalAmount += itemTotal;
                
                // FIX: Convert productID to String properly
                Cart cartItem = new Cart(
                    String.valueOf(product.getProductID()), // FIX: Convert int to String
                    product.getName(),
                    product.getPrice(),
                    quantity,
                    itemTotal
                );
                cartList.add(cartItem);
            }
            
            // Set attributes cho JSP
            request.setAttribute("cartList", cartList);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("ITEM_COUNT", cartList.size());
            request.setAttribute("CART_ID", cartID);
            
            // FIX: Forward đến đúng JSP file
            request.getRequestDispatcher("cartList.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error in cartList: " + e.toString());
            e.printStackTrace();
            
            // Set empty data
            request.setAttribute("cartList", new ArrayList<Cart>());
            request.setAttribute("totalAmount", 0.0f);
            request.setAttribute("ITEM_COUNT", 0);
            request.setAttribute("CART_ID", -1);
            request.setAttribute("MSG", "Không thể tải giỏ hàng: " + e.getMessage());
            
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
        return "Shopping Cart Controller";
    }
}