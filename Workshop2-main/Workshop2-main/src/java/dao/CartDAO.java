/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import utils.DBUtils;
import dto.Cart;

public class CartDAO {

    public boolean createCart(String userID, String createdDate) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tblCarts WHERE userID = ? AND createdDate = ?";
        try {
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, userID);
                checkPs.setString(2, createdDate);
                try ( ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            String insertSql = "INSERT INTO tblCarts (userID, createdDate) VALUES (?, ?)";
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, userID);
                ps.setString(2, createdDate);
                return ps.executeUpdate() > 0;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCart(String id) throws Exception {
        String sql = "DELETE FROM tblCarts WHERE userID = ?";
        boolean isDeleted = false;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, id);
            isDeleted = ps.executeUpdate() > 0;
        }
        return isDeleted;
    }

    public ArrayList<Cart> search(String search) throws SQLException {
        ArrayList<Cart> list = new ArrayList<>();
        String sql = "SELECT * FROM tblCarts WHERE userID LIKE ?";
        ResultSet rs = null;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn != null) {
                ps.setString(1, '%' + search + '%');
                rs = ps.executeQuery();
                while (rs.next()) {
                    String userID = rs.getString("userID");
                    String createdDate = rs.getString("createdDate");
                    list.add(new Cart(userID, createdDate));
                }
            }
        } catch (Exception e) {
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return list;
    }

    public Cart getCartById(String id) throws SQLException {
        String sql = "SELECT * FROM tblCarts WHERE userID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cart(rs.getString("userID"), rs.getString("createdDate"));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getCurrentCartID(String userID) throws SQLException {
        String sql = "SELECT TOP 1 cartID FROM tblCarts WHERE userID = ? ORDER BY createdDate DESC";

        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("cartID");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Không tìm thấy cart
    }

    public boolean updateCart(String userID, String createdDate) throws Exception {
        String sql = "UPDATE tblCarts SET createdDate = ? WHERE userID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, createdDate); // createdDate đặt trước
            stmt.setString(2, userID);         // cartID đặt sau
            return stmt.executeUpdate() > 0;
        }
    }
}
