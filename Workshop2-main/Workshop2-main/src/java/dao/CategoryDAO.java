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
import dto.Category;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CategoryDAO {

    private static final Logger LOGGER = Logger.getLogger(CategoryDAO.class.getName());

    public boolean createCategory(String categoryName, String description) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tblCategories WHERE categoryName = ? AND description = ?";
        try {
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, categoryName);
                checkPs.setString(2, description);
                try ( ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            String insertSql = "INSERT INTO tblCategories (categoryName, description) VALUES (?, ?)";
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, categoryName);
                ps.setString(2, description);
                return ps.executeUpdate() > 0;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCategory(int id) throws Exception {
        String sql = "DELETE FROM tblCategories WHERE categoryID = ?";
        boolean isDeleted = false;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            isDeleted = ps.executeUpdate() > 0;
        }
        return isDeleted;
    }

    public ArrayList<Category> search(String search) throws SQLException {
        ArrayList<Category> list = new ArrayList<>();
        String sql = "SELECT categoryID, categoryName, description, promoID FROM tblCategories WHERE categoryName LIKE ?";
        ResultSet rs = null;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn != null) {
                ps.setString(1, '%' + search + '%');
                rs = ps.executeQuery();
                while (rs.next()) {
                    int categoryID = rs.getInt("categoryID");
                    String categoryName = rs.getString("categoryName");
                    String description = rs.getString("description");
                    int promoID = rs.getInt("promoID");
                    list.add(new Category(categoryID, categoryName, description, promoID));
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in search: " + e.getMessage(), e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return list;
    }

    public Category getCategoryById(int id) throws SQLException {
        String sql = "SELECT categoryID, categoryName, description, promoID FROM tblCategories WHERE categoryID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("categoryID"), rs.getString("categoryName"), rs.getString("description"), rs.getInt("promoID"));
                }
            }
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error in getCategoryById: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean updateCategory(int id, String newName, String newDescription) throws Exception {
        String sql = "UPDATE tblCategories SET categoryName = ?, description = ? WHERE categoryID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean applyPromotion(int categoryID, int promoID) throws SQLException {
        Connection conn = null;
        PreparedStatement updateCategoryPs = null;
        PreparedStatement updateProductsPs = null;
        try {
            conn = DBUtils.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            String checkProductPromoSql = "SELECT COUNT(*) FROM tblProducts WHERE categoryID = ? AND promoID IS NOT NULL AND promoID != ?";
            try ( PreparedStatement checkPs = conn.prepareStatement(checkProductPromoSql)) {
                checkPs.setInt(1, categoryID);
                checkPs.setInt(2, promoID);
                try ( ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new SQLException("Some products in this category have different promotions");
                    }
                }
            }

            // Lấy discountPercent của khuyến mãi
            String selectPromoSql = "SELECT discountPercent FROM tblPromotions WHERE promoID = ?";
            float discountPercent = 0f;
            if (promoID > 0) {
                try ( PreparedStatement selectPromoPs = conn.prepareStatement(selectPromoSql)) {
                    selectPromoPs.setInt(1, promoID);
                    try ( ResultSet rs = selectPromoPs.executeQuery()) {
                        if (rs.next()) {
                            discountPercent = rs.getFloat("discountPercent");
                        } else {
                            return false; // Khuyến mãi không tồn tại
                        }
                    }
                }
            }

            // Cập nhật promoID cho category
            String updateCategorySql = "UPDATE tblCategories SET promoID = ? WHERE categoryID = ?";
            updateCategoryPs = conn.prepareStatement(updateCategorySql);
            if (promoID == 0) {
                updateCategoryPs.setNull(1, java.sql.Types.INTEGER);
            } else {
                updateCategoryPs.setInt(1, promoID);
            }
            updateCategoryPs.setInt(2, categoryID);
            int categoryUpdated = updateCategoryPs.executeUpdate();
            if (categoryUpdated == 0) {
                conn.rollback();
                return false; // Category không tồn tại
            }

            // Cập nhật giá sản phẩm thuộc category
            String updateProductsSql = "UPDATE tblProducts SET promoID = ?, price = originalPrice * ? WHERE categoryID = ?";
            updateProductsPs = conn.prepareStatement(updateProductsSql);
            if (promoID == 0) {
                updateProductsPs.setNull(1, java.sql.Types.INTEGER);
                updateProductsPs.setFloat(2, 1.0f); // Giá gốc
            } else {
                updateProductsPs.setInt(1, promoID);
                updateProductsPs.setFloat(2, 1.0f - discountPercent / 100.0f);
            }
            updateProductsPs.setInt(3, categoryID);
            updateProductsPs.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException se) {
                    LOGGER.log(Level.SEVERE, "Rollback failed: " + se.getMessage(), se);
                }
            }
            LOGGER.log(Level.SEVERE, "Error in applyPromotion: " + e.getMessage(), e);
            return false;
        } finally {
            if (updateProductsPs != null) try {
                updateProductsPs.close();
            } catch (SQLException ignored) {
            }
            if (updateCategoryPs != null) try {
                updateCategoryPs.close();
            } catch (SQLException ignored) {
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
