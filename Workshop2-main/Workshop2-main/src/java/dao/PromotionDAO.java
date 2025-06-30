package dao;

import dto.Promotion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import utils.DBUtils;

public class PromotionDAO {
    private static final Logger LOGGER = Logger.getLogger(PromotionDAO.class.getName());
    private Connection conn;

    public PromotionDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Promotion> getAllPromotions() throws SQLException {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT promoID, name, discountPercent, startDate, endDate, status FROM tblPromotions";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Promotion promo = new Promotion(
                    rs.getInt("promoID"),
                    rs.getString("name"),
                    rs.getInt("discountPercent"),
                    rs.getDate("startDate"),
                    rs.getDate("endDate"),
                    rs.getBoolean("status")
                );
                list.add(promo);
            }
        }
        return list;
    }

    public void addPromotion(Promotion promo) throws SQLException {
        String sql = "INSERT INTO tblPromotions (name, discountPercent, startDate, endDate, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promo.getName());
            ps.setInt(2, promo.getDiscountPercent());
            ps.setDate(3, promo.getStartDate());
            ps.setDate(4, promo.getEndDate());
            ps.setBoolean(5, promo.isStatus());
            ps.executeUpdate();
        }
    }

    public void deletePromotion(int promoID) throws SQLException {
        String sql = "DELETE FROM tblPromotions WHERE promoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, promoID);
            ps.executeUpdate();
        }
    }

    
    public void updateStatus(int promoID, boolean newStatus) throws SQLException {
        String sql = "UPDATE tblPromotions SET status = ? WHERE promoID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, newStatus);
            ps.setInt(2, promoID);
            ps.executeUpdate();
        }
    }
    
    public ArrayList<Promotion> search(String search) throws SQLException {
        ArrayList<Promotion> list = new ArrayList<>();
        String sql = "SELECT promoID, name, discountPercent, startDate, endDate, status FROM tblPromotions WHERE name LIKE ?";
        ResultSet rs = null;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn != null) {
                ps.setString(1, '%' + search + '%');
                rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new Promotion(
                    rs.getInt("promoID"),
                    rs.getString("name"),
                    rs.getInt("discountPercent"),
                    rs.getDate("startDate"),
                    rs.getDate("endDate"),
                    rs.getBoolean("status")
                ));
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
}
