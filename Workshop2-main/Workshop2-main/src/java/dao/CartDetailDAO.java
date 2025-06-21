package dao;

import dto.Product;
import dto.CartDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import utils.DBUtils;

public class CartDetailDAO {
    
    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(int cartID, int productID, int quantity) throws SQLException, ClassNotFoundException {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        String checkSql = "SELECT quantity FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        String updateSql = "UPDATE tblCartDetails SET quantity = quantity + ? WHERE cartID = ? AND productID = ?";
        String insertSql = "INSERT INTO tblCartDetails(cartID, productID, quantity) VALUES (?, ?, ?)";
        
        try (Connection conn = DBUtils.getConnection()) {
            // Kiểm tra sản phẩm đã tồn tại
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, cartID);
                ps.setInt(2, productID);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    // Sản phẩm đã có, cập nhật số lượng
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, quantity);
                        updatePs.setInt(2, cartID);
                        updatePs.setInt(3, productID);
                        return updatePs.executeUpdate() > 0;
                    }
                } else {
                    // Sản phẩm chưa có, thêm mới
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, cartID);
                        insertPs.setInt(2, productID);
                        insertPs.setInt(3, quantity);
                        return insertPs.executeUpdate() > 0;
                    }
                }
            }
        }
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ
    public boolean setQuantity(int cartID, int productID, int quantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblCartDetails SET quantity = ? WHERE cartID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartID);
            ps.setInt(3, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Xóa sản phẩm khỏi giỏ hàng
    public boolean removeFromCart(int cartID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Xóa toàn bộ giỏ hàng
    public boolean clearCart(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Lấy danh sách sản phẩm trong giỏ với thông tin chi tiết
    public Map<Product, Integer> getCartWithProducts(int cartID) throws SQLException, ClassNotFoundException {
        Map<Product, Integer> cartItems = new HashMap<>();
        String sql = "SELECT cd.productID, cd.quantity, p.productName, p.price, p.quantity as stockQuantity " +
                     "FROM tblCartDetails cd " +
                     "INNER JOIN tblProducts p ON cd.productID = p.productID " +
                     "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("productID"),
                    rs.getString("productName"),
                    rs.getFloat("price"),
                    rs.getInt("stockQuantity"),
                    true // status
                );
                cartItems.put(product, rs.getInt("quantity"));
            }
        }
        return cartItems;
    }
    
    // Tính tổng tiền trong giỏ hàng
    public float getTotalAmount(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(cd.quantity * p.price) as totalAmount " +
                     "FROM tblCartDetails cd " +
                     "INNER JOIN tblProducts p ON cd.productID = p.productID " +
                     "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getFloat("totalAmount");
            }
        }
        return 0.0f;
    }
    
    // Đếm số lượng items trong giỏ
    public int getCartItemCount(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) as itemCount FROM tblCartDetails WHERE cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("itemCount");
            }
        }
        return 0;
    }
    
    // Lấy số lượng của một sản phẩm cụ thể trong giỏ
    public int getProductQuantityInCart(int cartID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT quantity FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("quantity");
            }
        }
        return 0;
    }
}