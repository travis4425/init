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
import java.util.logging.Logger;
import java.util.logging.Level;
import utils.DBUtils;
import dto.Product;
import dto.Category;
import java.util.List;

public class ProductDAO {

    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());

    public boolean createProduct(String name, int categoryID, float price, int quantity, String status, String sellerID) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tblProducts WHERE name = ? AND categoryID = ? AND sellerID = ?";
        try {
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, name);
                checkPs.setInt(2, categoryID);
                checkPs.setString(3, sellerID);
                try ( ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            String insertSql = "INSERT INTO tblProducts (name, categoryID, price, quantity, status, sellerID, originalPrice) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, name);
                ps.setInt(2, categoryID);
                ps.setFloat(3, price);
                ps.setInt(4, quantity);
                ps.setString(5, status);
                ps.setString(6, sellerID);
                ps.setFloat(7, price); // Lưu giá gốc
                return ps.executeUpdate() > 0;
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in createProduct: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean deleteProduct(int id) throws Exception {
        String sql = "DELETE FROM tblProducts WHERE productID = ?";
        boolean isDeleted = false;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            isDeleted = ps.executeUpdate() > 0;
        }
        return isDeleted;
    }

    public boolean categoryExists(int categoryID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tblCategories WHERE categoryID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in categoryExists: " + e.getMessage(), e);
        };
        return false;
    }

    public boolean canUpdate(String newName, int newCategoryID, String sellerID) {
        String checkSql = "SELECT COUNT(*) FROM tblProducts WHERE name = ? AND categoryID = ? AND sellerID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setString(1, newName);
            checkPs.setInt(2, newCategoryID);
            checkPs.setString(3, sellerID);
            try ( ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in canUpdate: " + e.getMessage(), e);
        }
        return true;
    }

    public boolean updateProduct(int id, String newName, int newCategoryID, float newPrice, int newQuantity, String newStatus) throws Exception {
        String sql = "UPDATE tblProducts SET name = ?, categoryID = ?, price = ?, quantity = ?, status = ?, originalPrice = ? WHERE productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, newCategoryID);
            stmt.setFloat(3, newPrice);
            stmt.setInt(4, newQuantity);
            stmt.setString(5, newStatus);
            stmt.setFloat(6, newPrice); // Cập nhật giá gốc
            stmt.setInt(7, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<Product> search(String nameSearch, String cateSearch, float priceSearch, String statusSearch) throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "SELECT p.productID, p.name, c.categoryName, p.price, p.quantity, p.sellerID, u.fullname, p.status, p.promoID, p.originalPrice\n"
                + "FROM tblProducts p\n"
                + "JOIN tblCategories c ON p.categoryID = c.categoryID\n"
                + "LEFT JOIN tblUsers u ON p.sellerID = u.userID\n"
                + "WHERE p.name LIKE ? AND c.categoryName LIKE ? AND p.status LIKE ?";
        if (priceSearch > 0) {
            sql += " AND p.price <= ?";
        }
        ResultSet rs = null;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn != null) {
                ps.setString(1, '%' + nameSearch + '%');
                ps.setString(2, '%' + cateSearch + '%');
                ps.setString(3, '%' + statusSearch + '%');
                if (priceSearch > 0) {
                    ps.setFloat(4, priceSearch);
                }
                rs = ps.executeQuery();
                while (rs.next()) {
                    int productID = rs.getInt("productID");
                    String name = rs.getString("name");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");
                    String sellerID = rs.getString("sellerID");
                    if (sellerID == null) {
                        sellerID = "";
                    }
                    String sellerFullName = rs.getString("fullname") != null ? rs.getString("fullname") : "Unknown";
                    String status = rs.getString("status");
                    String cateName = rs.getString("categoryName");
                    int promoID = rs.getInt("promoID");
                    float originalPrice = rs.getFloat("originalPrice");
                    list.add(new Product(productID, quantity, price, name, sellerID, status, cateName, sellerFullName, promoID, originalPrice));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in search: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in search: " + e.getMessage(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return list;
    }

    public List<Product> getProductsByUser(String userID, String nameSearch, String cateSearch, float priceSearch, String statusSearch) throws SQLException {
        String sql = "SELECT p.productID, p.name, c.categoryName, p.price, p.quantity, p.sellerID, u.fullname, p.status, p.promoID, p.originalPrice\n"
                + "FROM tblProducts p\n"
                + "JOIN tblCategories c ON p.categoryID = c.categoryID\n"
                + "LEFT JOIN tblUsers u ON p.sellerID = u.userID\n"
                + "WHERE p.sellerID = ?\n"
                + "AND p.name LIKE ? AND c.categoryName LIKE ? AND p.status LIKE ?";
        if (priceSearch > 0) {
            sql += " AND p.price <= ?";
        }
        List<Product> list = new ArrayList<>();
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userID);
            ps.setString(2, '%' + nameSearch + '%');
            ps.setString(3, '%' + cateSearch + '%');
            ps.setString(4, '%' + statusSearch + '%');
            if (priceSearch > 0) {
                ps.setFloat(5, priceSearch);
            }
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productID = rs.getInt("productID");
                    String name = rs.getString("name");
                    Float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");
                    String sellerID = rs.getString("sellerID");
                    if (sellerID == null) {
                        sellerID = "";
                    }
                    String sellerFullName = rs.getString("fullname") != null ? rs.getString("fullname") : "Unknown";
                    String status = rs.getString("status");
                    String cateName = rs.getString("categoryName");
                    int promoID = rs.getInt("promoID");
                    float originalPrice = rs.getFloat("originalPrice");
                    list.add(new Product(productID, quantity, price, name, sellerID, status, cateName, sellerFullName, promoID, originalPrice));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in searchProductsByUser: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in searchProductsByUser: " + e.getMessage(), e);
        }
        return list;
    }

    public Product getProductById(int id) throws SQLException {
        String sql = "SELECT p.productID, p.name, p.categoryID, p.price, p.quantity, p.status, p.sellerID, u.fullname, p.promoID, p.originalPrice "
                + "FROM tblProducts p "
                + "LEFT JOIN tblUsers u ON p.sellerID = u.userID "
                + "WHERE p.productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String sellerID = rs.getString("sellerID");
                    return new Product(rs.getInt("productID"), rs.getInt("quantity"), rs.getFloat("price"),
                            rs.getString("name"), sellerID, rs.getString("status"), rs.getInt("promoID"), rs.getFloat("originalPrice"));
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in getProductById: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT categoryID, categoryName, description FROM tblCategories";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("categoryID"),
                        rs.getString("categoryName"),
                        rs.getString("description")
                ));
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in getAllCategories: " + e.getMessage(), e);
        }
        return categories;
    }

    public boolean applyPromotion(int productID, int promoID) throws SQLException {
        // Lấy giá hiện tại, giá gốc, và discountPercent (nếu có)
        String selectSql = "SELECT p.price, p.originalPrice, pr.discountPercent "
                + "FROM tblProducts p "
                + "LEFT JOIN tblPromotions pr ON pr.promoID = ? "
                + "WHERE p.productID = ?";
        float currentPrice = 0f;
        float originalPrice = 0f;
        float discountPercent = 0f;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement selectPs = conn.prepareStatement(selectSql)) {
            selectPs.setInt(1, promoID);
            selectPs.setInt(2, productID);
            try ( ResultSet rs = selectPs.executeQuery()) {
                if (rs.next()) {
                    currentPrice = rs.getFloat("price");
                    originalPrice = rs.getFloat("originalPrice");
                    if (rs.getObject("discountPercent") != null) {
                        discountPercent = rs.getFloat("discountPercent");
                    }
                } else {
                    return false; // Sản phẩm không tồn tại
                }
            }
        } catch (ClassNotFoundException e) {
        }

        // Tính giá mới
        float newPrice = originalPrice; // Mặc định dùng giá gốc
        if (promoID > 0) {
            newPrice = originalPrice * (1 - discountPercent / 100);
        }

        // Cập nhật promoID và giá
        String updateSql = "UPDATE tblProducts SET promoID = ?, price = ? WHERE productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
            if (promoID == 0) {
                updatePs.setNull(1, java.sql.Types.INTEGER);
            } else {
                updatePs.setInt(1, promoID);
            }
            updatePs.setFloat(2, newPrice);
            updatePs.setInt(3, productID);
            return updatePs.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
        }
        return false;
    }
}
