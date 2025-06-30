/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Return;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ReturnDAO {
    
    public boolean createReturn(int invoiceID, String reason) throws Exception {
        String sql = "INSERT INTO tblReturns (invoiceID, reason, status) VALUES (?, ?, 'Pending')";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setString(2, reason);
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<Return> getReturns(String keyword, String status) throws Exception {
        List<Return> list = new ArrayList<>();
        String sql = "SELECT * FROM tblReturns WHERE (CAST(invoiceID AS VARCHAR) LIKE ? OR ? = '') AND (status = ? OR ? = '') ORDER BY returnID DESC";
        try (Connection con = DBUtils.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, keyword);
            ps.setString(3, status);
            ps.setString(4, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Return(
                    rs.getInt("returnID"),
                    rs.getInt("invoiceID"),
                    rs.getString("reason"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }
    
    public boolean isReturned(int invoiceID) throws Exception {
        String sql = "SELECT returnID FROM tblReturns WHERE invoiceID = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public List<Return> getReturnsByUser(String keyword, String status, String userID) throws Exception {
        List<Return> list = new ArrayList<>();
        String sql
                = "SELECT r.returnID, r.invoiceID, r.reason, r.status "
                + "FROM tblReturns r "
                + "JOIN tblInvoices i ON r.invoiceID = i.invoiceID "
                + "WHERE i.userID = ? AND "
                + "(CAST(r.invoiceID AS VARCHAR) LIKE ? OR ? = '') AND "
                + "(r.status = ? OR ? = '') ORDER BY r.returnID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, keyword);
            ps.setString(4, status);
            ps.setString(5, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Return(
                        rs.getInt("returnID"),
                        rs.getInt("invoiceID"),
                        rs.getString("reason"),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }
    
    public boolean updateStatus(int returnID, String status) throws Exception {
        String sql = "UPDATE tblReturns SET status = ? WHERE returnID = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, returnID);
            return ps.executeUpdate() > 0;
        }
    }
}
