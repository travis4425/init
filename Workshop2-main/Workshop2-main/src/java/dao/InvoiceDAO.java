/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.Invoice;
import dto.InvoiceDetail;
import dto.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.DBUtils;

public class InvoiceDAO {
    
    // Tạo hóa đơn mới
    public int createInvoice(String userID, float totalAmount) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblInvoices(userID, status, totalAmount, createdDate) VALUES (?, ?, ?, ?) SELECT SCOPE_IDENTITY()";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setString(2, "Pending"); // Trạng thái mặc định
            ps.setFloat(3, totalAmount);
            ps.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    
    // Lấy thông tin hóa đơn theo ID
    public Invoice getInvoiceByID(int invoiceID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblInvoices WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Invoice(
                    rs.getInt("invoiceID"),
                    rs.getString("userID"),
                    rs.getString("status"),
                    rs.getFloat("totalAmount"),
                    rs.getDate("createdDate").toLocalDate()
                );
            }
        }
        return null;
    }
    
    // Lấy danh sách hóa đơn của user
    public List<Invoice> getInvoicesByUser(String userID) throws SQLException, ClassNotFoundException {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM tblInvoices WHERE userID = ? ORDER BY createdDate DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                    rs.getInt("invoiceID"),
                    rs.getString("userID"),
                    rs.getString("status"),
                    rs.getFloat("totalAmount"),
                    rs.getDate("createdDate").toLocalDate()
                );
                list.add(invoice);
            }
        }
        return list;
    }
    
    // Cập nhật trạng thái hóa đơn
    public boolean updateInvoiceStatus(int invoiceID, String status) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblInvoices SET status = ? WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, invoiceID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Lấy tất cả hóa đơn (cho admin)
    public List<Invoice> getAllInvoices() throws SQLException, ClassNotFoundException {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM tblInvoices ORDER BY createdDate DESC";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                    rs.getInt("invoiceID"),
                    rs.getString("userID"),
                    rs.getString("status"),
                    rs.getFloat("totalAmount"),
                    rs.getDate("createdDate").toLocalDate()
                );
                list.add(invoice);
            }
        }
        return list;
    }
    
    // Tìm kiếm hóa đơn
    public List<Invoice> searchInvoices(String userID, String status, LocalDate fromDate, LocalDate toDate) 
            throws SQLException, ClassNotFoundException {
        List<Invoice> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM tblInvoices WHERE 1=1");
        
        if (userID != null && !userID.isEmpty()) {
            sql.append(" AND userID LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (fromDate != null) {
            sql.append(" AND createdDate >= ?");
        }
        if (toDate != null) {
            sql.append(" AND createdDate <= ?");
        }
        sql.append(" ORDER BY createdDate DESC");
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int index = 1;
            if (userID != null && !userID.isEmpty()) {
                ps.setString(index++, "%" + userID + "%");
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(index++, status);
            }
            if (fromDate != null) {
                ps.setDate(index++, java.sql.Date.valueOf(fromDate));
            }
            if (toDate != null) {
                ps.setDate(index++, java.sql.Date.valueOf(toDate));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                    rs.getInt("invoiceID"),
                    rs.getString("userID"),
                    rs.getString("status"),
                    rs.getFloat("totalAmount"),
                    rs.getDate("createdDate").toLocalDate()
                );
                list.add(invoice);
            }
        }
        return list;
    }
    
    // Xóa hóa đơn (chỉ admin)
    public boolean deleteInvoice(int invoiceID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblInvoices WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            return ps.executeUpdate() > 0;
        }
    }
}