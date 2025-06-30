/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Cart;
import dto.CartDetail;
import dto.Product;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartDAO {

    // Thêm giỏ hàng mới, trả về cartID mới được tạo
    public int insertCart(Cart cart) throws Exception {
        String sql = "INSERT INTO tblCarts (userID, createdDate) VALUES (?, ?)";
        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cart.getUserID());
            ps.setDate(2, Date.valueOf(cart.getCreatedDate()));
            ps.executeUpdate();

            try ( ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // Trả về cartID vừa tạo
                }
            }
        }
        return -1;
    }

    // Lấy giỏ hàng theo userID
    public Cart getCartByUserID(String userID) throws Exception {
        String sql = "SELECT * FROM tblCarts WHERE userID = ?";
        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int cartID = rs.getInt("cartID");
                    LocalDate createdDate = rs.getDate("createdDate").toLocalDate();

                    // Lấy danh sách sản phẩm trong giỏ
                    CartDetailDAO detailDAO = new CartDetailDAO();
                    List<CartDetail> details = detailDAO.getCartDetails(cartID);

                    return new Cart(cartID, userID, createdDate, details);
                }
            }
        }
        return null;
    }

    public List<CartDetail> viewCart(String userID) throws Exception {
        Cart cart = getCartByUserID(userID);
        if (cart != null) {
            CartDetailDAO detailDAO = new CartDetailDAO();
            return detailDAO.getCartDetails(cart.getCartID());
        }
        return new ArrayList<>();
    }

    public List<CartDetail> searchCart(String userID, String nameSearch, String cateSearch, Double maxPrice) throws Exception {
        List<CartDetail> result = new ArrayList<>();

        String sql = "SELECT cd.cartID, cd.productID, cd.quantity, p.name, p.price, c.categoryName "
                + "FROM tblCartDetails cd "
                + "JOIN tblProducts p ON cd.productID = p.productID "
                + "JOIN tblCategories c ON p.categoryID = c.categoryID "
                + "JOIN tblCarts cart ON cd.cartID = cart.cartID "
                + "WHERE cart.userID = ? ";

        if (nameSearch != null && !nameSearch.trim().isEmpty()) {
            sql += "AND p.name LIKE ? ";
        }
        if (cateSearch != null && !cateSearch.trim().isEmpty()) {
            sql += "AND c.categoryName = ? ";
        }
        if (maxPrice != null) {
            sql += "AND p.price <= ? ";
        }

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            int index = 1;
            ps.setString(index++, userID);

            if (nameSearch != null && !nameSearch.trim().isEmpty()) {
                ps.setString(index++, "%" + nameSearch + "%");
            }
            if (cateSearch != null && !cateSearch.trim().isEmpty()) {
                ps.setString(index++, cateSearch);
            }
            if (maxPrice != null) {
                ps.setDouble(index++, maxPrice);
            }

            try ( ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int cartID = rs.getInt("cartID");
                    int productID = rs.getInt("productID");
                    String name = rs.getString("name");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");
                    String cateName = rs.getString("categoryName");

                    Product product = new Product(productID, name, price, cateName);
                    CartDetail detail = new CartDetail(cartID, product, quantity);
                    result.add(detail);
                }
            }
        }
        return result;
    }
}
