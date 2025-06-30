/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.CartDetail;
import dto.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;


public class CartDetailDAO {
    // Thêm sản phẩm vào giỏ
    public void insertCartDetail(CartDetail detail) throws Exception {
        String sql = "INSERT INTO tblCartDetails (cartID, productID, quantity) VALUES (?, ?, ?)";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detail.getCartID());
            ps.setInt(2, detail.getProduct().getProductID());
            ps.setInt(3, detail.getQuantity());
            ps.executeUpdate();
        }
    }
    
    // Lấy danh sách sản phẩm theo cartID
    public List<CartDetail> getCartDetails(int cartID) throws Exception {
        List<CartDetail> list = new ArrayList<>();
        String sql = "SELECT cd.productID, cd.quantity, p.name, p.price, c.categoryName FROM tblCartDetails cd " +
                     "JOIN tblProducts p ON cd.productID = p.productID " +
                     "JOIN tblCategories c on p.categoryID = c.categoryID " + 
                     "WHERE cd.cartID = ?";

        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productID = rs.getInt("productID");
                    String name = rs.getString("name");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");
                    String cateName = rs.getString("categoryName");

                    Product product = new Product(productID, name, price, cateName);
                    list.add(new CartDetail(cartID, product, quantity));
                }
            }
        }
        return list;
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ
    public void updateQuantity(int cartID, int productID, int quantity) throws Exception {
        String sql = "UPDATE tblCartDetails SET quantity = ? WHERE cartID = ? AND productID = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartID);
            ps.setInt(3, productID);
            ps.executeUpdate();
        }
    }
    
    // Xóa sản phẩm khỏi giỏ
    public void deleteCartDetail(int cartID, int productID) throws Exception {
        String sql = "DELETE FROM tblCartDetails WHERE cartID = ? AND productID = ?";
        try (Connection con = DBUtils.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartID);
            ps.setInt(2, productID);
            ps.executeUpdate();
        }
    }
}
