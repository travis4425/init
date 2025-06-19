/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.Cart;
import dto.CartDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartDAO {
    
    // Tạo cart mới cho user
    public int createCart(String userID) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblCarts(userID, createdDate) VALUES (?, ?) SELECT SCOPE_IDENTITY()";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
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
    
    // Lấy thông tin cart
    public Cart getCartByID(int cartID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblCarts WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(cartID));
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
}