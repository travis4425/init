package dao;

import dto.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductDAO {
    
    // Lấy sản phẩm theo ID
    public Product getProduct(String productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblProducts WHERE productID = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                    rs.getInt("productID"),
                    rs.getInt("categoryID"),
                    rs.getInt("quantity"),
                    rs.getFloat("price"),
                    rs.getString("name"),
                    rs.getString("seller"),
                    rs.getString("status")
                );
            }
        }
        return null;
    }
    
    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM tblProducts WHERE status = 'Active'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
                list.add(product);
            }
        }
        return list;
    }
    
    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchProducts(String searchValue) throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM tblProducts WHERE name LIKE ? AND status = 'Active'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchValue + "%");
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
                list.add(product);
            }
        }
        return list;
    }
    
    // Cập nhật số lượng sản phẩm sau khi bán
    public boolean updateProductQuantity(int productID, int quantitySold) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblProducts SET quantity = quantity - ? WHERE productID = ? AND quantity >= ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantitySold);
            ps.setInt(2, productID);
            ps.setInt(3, quantitySold);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Lấy sản phẩm theo category
    public List<Product> getProductsByCategory(int categoryID) throws SQLException, ClassNotFoundException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM tblProducts WHERE categoryID = ? AND status = 'Active'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryID);
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
                list.add(product);
            }
        }
        return list;
    }
    
    // Kiểm tra sản phẩm có đủ số lượng không
    public boolean checkProductAvailability(int productID, int requestedQuantity) throws SQLException, ClassNotFoundException {
        String sql = "SELECT quantity FROM tblProducts WHERE productID = ? AND status = 'Active'";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity") >= requestedQuantity;
            }
        }
        return false;
    }
}