/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.InvoiceDetail;
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

public class InvoiceDetailDAO {
    
    // Thêm chi tiết hóa đơn
    public boolean addInvoiceDetail(int invoiceID, int productID, int quantity, float price) 
            throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblInvoiceDetails(invoiceID, productID, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setInt(2, productID);
            ps.setInt(3, quantity);
            ps.setFloat(4, price);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Lấy danh sách chi tiết hóa đơn
    public List<InvoiceDetail> getInvoiceDetails(int invoiceID) throws SQLException, ClassNotFoundException {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM tblInvoiceDetails WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail(
                    rs.getInt("invoiceID"),
                    rs.getInt("productID"),
                    rs.getInt("quantity"),
                    rs.getFloat("price")
                );
                list.add(detail);
            }
        }
        return list;
    }
    
    // Lấy chi tiết hóa đơn kèm thông tin sản phẩm
    public Map<Product, InvoiceDetail> getInvoiceDetailsWithProducts(int invoiceID) 
            throws SQLException, ClassNotFoundException {
        Map<Product, InvoiceDetail> detailMap = new HashMap<>();
        String sql = "SELECT id.*, p.* FROM tblInvoiceDetails id " +
                    "JOIN tblProducts p ON id.productID = p.productID " +
                    "WHERE id.invoiceID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product(
                    rs.getInt("productID"),
                    rs.getInt("categoryID"),
                    rs.getInt("quantity"), // Số lượng trong kho
                    rs.getFloat("price"),
                    rs.getString("name"),
                    rs.getString("seller"),
                    rs.getString("status")
                );
                
                InvoiceDetail detail = new InvoiceDetail(
                    rs.getInt("invoiceID"),
                    rs.getInt("productID"),
                    rs.getInt("quantity"), // Số lượng đã mua (từ bảng invoice detail)
                    rs.getFloat("price")   // Giá tại thời điểm mua
                );
                
                detailMap.put(product, detail);
            }
        }
        return detailMap;
    }
    
    // Tạo chi tiết hóa đơn từ giỏ hàng
    public boolean createInvoiceDetailsFromCart(int invoiceID, int cartID) 
            throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblInvoiceDetails(invoiceID, productID, quantity, price) " +
                    "SELECT ?, cd.productID, cd.quantity, p.price " +
                    "FROM tblCartDetails cd " +
                    "JOIN tblProducts p ON cd.productID = p.productID " +
                    "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setInt(2, cartID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Xóa chi tiết hóa đơn
    public boolean deleteInvoiceDetail(int invoiceID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblInvoiceDetails WHERE invoiceID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setInt(2, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Xóa tất cả chi tiết của hóa đơn
    public boolean deleteAllInvoiceDetails(int invoiceID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblInvoiceDetails WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            return ps.executeUpdate() >= 0;
        }
    }
    
    // Cập nhật số lượng trong chi tiết hóa đơn
    public boolean updateQuantity(int invoiceID, int productID, int newQuantity) 
            throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblInvoiceDetails SET quantity = ? WHERE invoiceID = ? AND productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, invoiceID);
            ps.setInt(3, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Tính tổng tiền của hóa đơn
    public float calculateTotalAmount(int invoiceID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT SUM(quantity * price) as total FROM tblInvoiceDetails WHERE invoiceID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat("total");
            }
        }
        return 0.0f;
    }
}