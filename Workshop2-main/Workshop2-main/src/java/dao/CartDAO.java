package dao;

import dto.CartDetail;
import dto.Cart;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartDAO {
    
    // Tạo cart mới cho user
    public int createCart(String userID) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblCarts(userID, createdDate) VALUES (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userID);
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }
    
    // Lấy cart hiện tại của user (hoặc tạo mới nếu chưa có)
    public int getCurrentCartID(String userID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT TOP 1 cartID FROM tblCarts WHERE userID = ? ORDER BY createdDate DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cartID");
            } else {
                // Tạo cart mới nếu chưa có
                return createCart(userID);
            }
        }
    }
    
    // FIX: Tạo Cart DTO class riêng
    public static class Cart {
        private int cartID;
        private String userID;
        private LocalDate createdDate;
        
        public Cart(int cartID, String userID, LocalDate createdDate) {
            this.cartID = cartID;
            this.userID = userID;
            this.createdDate = createdDate;
        }
        
        // Getters and setters
        public int getCartID() { return cartID; }
        public void setCartID(int cartID) { this.cartID = cartID; }
        
        public String getUserID() { return userID; }
        public void setUserID(String userID) { this.userID = userID; }
        
        public LocalDate getCreatedDate() { return createdDate; }
        public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }
    }
    
    // Lấy thông tin cart - FIX return type
    public Cart getCartByID(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblCarts WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cart(
                    rs.getInt("cartID"),
                    rs.getString("userID"),
                    rs.getDate("createdDate").toLocalDate()
                );
            }
        }
        return null;
    }
    
    // Xóa cart (sau khi thanh toán)
    public boolean deleteCart(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblCarts WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Kiểm tra cart có tồn tại không
    public boolean cartExists(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM tblCarts WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}