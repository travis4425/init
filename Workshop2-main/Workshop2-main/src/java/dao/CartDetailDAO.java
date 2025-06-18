/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.CartDetail;
import dto.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.DBUtils;

public class CartDetailDAO {
    
    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(int cartID, int productID, int quantity) throws SQLException, ClassNotFoundException {
        // Kiểm tra xem sản phẩm đã có trong giỏ chưa
        if (isProductInCart(cartID, productID)) {
            // Nếu có rồi thì cập nhật số lượng
            return updateQuantity(cartID, productID, quantity);
        } else {
            // Nếu chưa có thì thêm mới
            String sql = "INSERT INTO tblCartDetails(cartID, productID, quantity) VALUES (?, ?, ?)";
            try (Connection conn = DBUtils.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, cartID);
                ps.setInt(2, productID);
                ps.setInt(3, quantity);
                return ps.executeUpdate() > 0;
            }
        }
    }
    
    // Kiểm tra sản phẩm có trong giỏ hàng không
    private boolean isProductInCart(int cartID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ
    public boolean updateQuantity(int cartID, int productID, int newQuantity) throws SQLException, ClassNotFoundException {
        if (newQuantity <= 0) {
            return removeFromCart(cartID, productID);
        }
        
        String sql = "UPDATE tblCartDetails SET quantity = quantity + ? WHERE cartID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartID);
            ps.setInt(3, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Set số lượng cụ thể
    public boolean setQuantity(int cartID, int productID, int quantity) throws SQLException, ClassNotFoundException {
        if (quantity <= 0) {
            return removeFromCart(cartID, productID);
        }
        
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
    
    // Lấy danh sách chi tiết giỏ hàng
    public List<CartDetail> getCartDetails(int cartID) throws SQLException, ClassNotFoundException {
        List<CartDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM tblCartDetails WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartDetail detail = new CartDetail(
                    rs.getInt("cartID"),
                    rs.getInt("productID"),
                    rs.getInt("quantity")
                );
                list.add(detail);
            }
        }
        return list;
    }
    
    // Lấy danh sách sản phẩm trong giỏ kèm thông tin sản phẩm
    public Map<Product, Integer> getCartWithProducts(int cartID) throws SQLException, ClassNotFoundException {
        Map<Product, Integer> cartItems = new HashMap<>();
        String sql = "SELECT cd.quantity, p.* FROM tblCartDetails cd " +
                    "JOIN tblProducts p ON cd.productID = p.productID " +
                    "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productID"),
                    rs.getInt("categoryID"),
                    rs.getInt("quantity"),
                    rs.getFloat("price"),
                    rs.getString("name"),
                    rs.getString("seller"),
                    rs.getString("status")
                );
                int quantity = rs.getInt("quantity");
                cartItems.put(product, quantity);
            }
        }
        return cartItems;
    }
    
    // Tính tổng tiền giỏ hàng
    public float getTotalAmount(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(cd.quantity * p.price) as total " +
                    "FROM tblCartDetails cd " +
                    "JOIN tblProducts p ON cd.productID = p.productID " +
                    "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("total");
            }
        }
        return 0.0f;
    }
    
    // Đếm số lượng item trong giỏ
    public int getCartItemCount(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity) as total FROM tblCartDetails WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }
    
    // Xóa tất cả items trong cart
    public boolean clearCart(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            return ps.executeUpdate() >= 0; // >= 0 vì có thể cart rỗng
        }
    }
}