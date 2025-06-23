package dao;

import dto.Cart;
import dto.Product;
import dto.CartDetail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import utils.DBUtils;

public class CartDetailDAO {

    public boolean createCartDetail(int cartID, int productID, int quantity) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tblCartDetails WHERE cartID = ? AND productID = ? AND quantity = ?";
        try {
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setInt(1, cartID);
                checkPs.setInt(2, productID);
                checkPs.setInt(3, quantity);
                try ( ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }
            String insertSql = "INSERT INTO tblCartDetails (cartID, productID, quantity) VALUES (?, ?, ?)";
            try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, cartID);
                ps.setInt(2, productID);
                ps.setInt(2, quantity);
                return ps.executeUpdate() > 0;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCartDetail(int id) throws Exception {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ?";
        boolean isDeleted = false;
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, id);
            isDeleted = ps.executeUpdate() > 0;
        }
        return isDeleted;
    }

    public ArrayList<CartDetail> getCartDetailsByCartID(int cartID) throws SQLException {
        ArrayList<CartDetail> cartDetails = new ArrayList<>();
        String sql = "SELECT cartID, productID, quantity FROM tblCartDetails WHERE cartID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cartId = rs.getInt("cartID");
                    int productID = rs.getInt("productID");
                    int quantity = rs.getInt("quantity");

                    CartDetail cartDetail = new CartDetail(cartId, productID, quantity);
                    cartDetails.add(cartDetail);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cartDetails;
    }

    public CartDetail getCartDetail(int cartID, int productID) throws SQLException {
        String sql = "SELECT cartID, productID, quantity FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CartDetail(
                            rs.getInt("cartID"),
                            rs.getInt("productID"),
                            rs.getInt("quantity")
                    );
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCartDetailQuantity(int cartID, int productID, int newQuantity) throws SQLException {
        String sql = "UPDATE tblCartDetails SET quantity = ? WHERE cartID = ? AND productID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartID);
            ps.setInt(3, productID);
            return ps.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clearCartDetails(int cartID) throws SQLException {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ?";
        try ( Connection conn = DBUtils.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            return ps.executeUpdate() >= 0; // Even if no rows affected, it's successful
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Get total quantity of items in a cart
/*
    public int getTotalQuantityInCart(int cartID) throws SQLException {
        String sql = "SELECT SUM(quantity) as totalQuantity FROM tblCartDetails WHERE cartID = ?";
        try (Connection conn = DBUtils.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalQuantity");
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
     */
    //Get cart details with product information (JOIN query)
/*
public Map<Product, Integer> getCartDetailsWithProducts(int cartID) throws SQLException {
        Map<Product, Integer> cartItems = new HashMap<>();
        String sql = "SELECT cd.quantity, p.productID, p.name, p.price, p.categoryID, p.sellerID, p.status, p.promoID " +
                    "FROM tblCartDetails cd " +
                    "INNER JOIN tblProducts p ON cd.productID = p.productID " +
                    "WHERE cd.cartID = ?";
        
        try (Connection conn = DBUtils.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("name"),
                        rs.getInt("categoryID"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"), // This would be product stock, not cart quantity
                        rs.getString("sellerID"),
                        rs.getString("status"),
                        rs.getInt("promoID")
                    );
                    
                    int quantity = rs.getInt("quantity");
                    cartItems.put(product, quantity);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cartItems;
    }    
     */
}
