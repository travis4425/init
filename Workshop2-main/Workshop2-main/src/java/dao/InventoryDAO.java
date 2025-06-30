package dao;

import dto.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class InventoryDAO {
    
    // Method for CreateInventoryController
    public boolean createInventory(int warehouseID, int productID, int stockQuantity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO tblInventories (warehouseID, productID, stockQuantity) VALUES (?, ?, ?)";
        try (Connection con = DBUtils.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, warehouseID);
            ps.setInt(2, productID);
            ps.setInt(3, stockQuantity);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Method for UpdateInventoryController
    public boolean updateInventory(int warehouseID, int productID, int stockQuantity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE tblInventories SET stockQuantity = ? WHERE warehouseID = ? AND productID = ?";
        try (Connection con = DBUtils.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, stockQuantity);
            ps.setInt(2, warehouseID);
            ps.setInt(3, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Method for DeleteInventoryController
    public boolean deleteInventory(int warehouseID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM tblInventories WHERE warehouseID = ? AND productID = ?";
        try (Connection con = DBUtils.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, warehouseID);
            ps.setInt(2, productID);
            return ps.executeUpdate() > 0;
        }
    }
    
    // Method for SearchInventoryController
    public List<Inventory> searchInventories(Integer warehouseID, Integer productID) throws SQLException, ClassNotFoundException {
        List<Inventory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT warehouseID, productID, stockQuantity FROM tblInventories WHERE 1=1");
        
        if (warehouseID != null) {
            sql.append(" AND warehouseID = ?");
        }
        if (productID != null) {
            sql.append(" AND productID = ?");
        }
        
        try (Connection con = DBUtils.getConnection(); 
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            int idx = 1;
            if (warehouseID != null) {
                ps.setInt(idx++, warehouseID);
            }
            if (productID != null) {
                ps.setInt(idx++, productID);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Inventory inv = new Inventory();
                    inv.setWarehouseID(rs.getInt("warehouseID"));
                    inv.setProductID(rs.getInt("productID"));
                    inv.setStockQuantity(rs.getInt("stockQuantity"));
                    list.add(inv);
                }
            }
        }
        return list;
    }
    
    // Additional helper methods
    public Inventory get(int warehouseID, int productID) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM tblInventories WHERE warehouseID=? AND productID=?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, warehouseID);
            ps.setInt(2, productID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(rs.getInt("warehouseID"), 
                                       rs.getInt("productID"), 
                                       rs.getInt("stockQuantity"));
                }
            }
        }
        return null;
    }
    
    public List<Inventory> getByWarehouse(int warehouseID) throws SQLException, ClassNotFoundException {
        List<Inventory> list = new ArrayList<>();
        String sql = "SELECT * FROM tblInventories WHERE warehouseID=?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, warehouseID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Inventory(rs.getInt("warehouseID"), 
                                         rs.getInt("productID"), 
                                         rs.getInt("stockQuantity")));
                }
            }
        }
        return list;
    }
}