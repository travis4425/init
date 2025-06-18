/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class UserDAO {
    public User login(String userID, String password) throws ClassNotFoundException, SQLException {
        // Debug đầu vào
        System.out.println("UserDAO.login(): userID=" + userID + ", password=" + password);

        String sql = "SELECT fullName, roleID, phone FROM tblUsers WHERE userID = ? AND password = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userID);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    String roleID   = rs.getString("roleID");
                    String phone = rs.getString("phone");
                    System.out.println("Login successful for " + userID + ", fullName=" + fullName + ", roleID=" + roleID);
                    // *** LƯU Ý: tham số cuối "***" chỉ dùng tạm, không quan trọng
                    return new User(userID, fullName, roleID, "***", phone);
                } else {
                    System.out.println("Login failed: no matching record");
                }
            }
        }
        return null;
    }
    
    public List<User> search(String userID, String fullName, String roleID) throws Exception {
        List<User> list = new ArrayList<>();
        String sql = "SELECT userID, fullName, roleID, password, phone FROM tblUsers WHERE 1=1";

        if (userID != null && !userID.isEmpty()) {
            sql += " AND userID LIKE ?";
        }
        if (fullName != null && !fullName.isEmpty()) {
            sql += " AND fullName LIKE ?";
        }
        if (roleID != null && !roleID.isEmpty()) {
            sql += " AND roleID = ?";
        }

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            if (userID != null && !userID.isEmpty()) {
                ps.setString(index++, "%" + userID + "%");
            }
            if (fullName != null && !fullName.isEmpty()) {
                ps.setString(index++, "%" + fullName + "%");
            }
            if (roleID != null && !roleID.isEmpty()) {
                ps.setString(index++, roleID);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
    }
    
    public boolean create(User user) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblUsers(userID, fullName, roleID, password, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserID());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getRoleID());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getPhone());
            return ps.executeUpdate() > 0;
       }
    }
    
    public boolean update(User s)
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblUsers SET fullName=?, roleID=?, password=?, phone=? WHERE userID=?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getFullName());
            ps.setString(2, s.getRoleID());
            ps.setString(3, s.getPassword());
            ps.setString(4, s.getPhone());
            ps.setString(5, s.getUserID());
            return ps.executeUpdate() > 0;
        }
    }
    
    public User getUserByID(String userID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblUsers WHERE userID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }
    
    public boolean delete(String userID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblUsers WHERE userID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            return ps.executeUpdate() > 0;
        }
    }
    
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("userID"),
                rs.getString("fullName"),
                rs.getString("roleID"),
                rs.getString("password"),
                rs.getString("phone")
        );
    }
}
