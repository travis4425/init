/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Delivery;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class DeliveryDAO {

    public boolean createDelivery(int invoiceID, String address, String status) throws Exception {
        String sql = "INSERT INTO tblDeliveries (invoiceID, address, deliveryDate, status) VALUES (?, ?, GETDATE(), ?)";
        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceID);
            ps.setString(2, address);
            ps.setString(3, status);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Delivery> getAllDeliveries() throws Exception {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT deliveryID, invoiceID, address, deliveryDate, status FROM tblDeliveries ORDER BY deliveryID DESC";
        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Delivery(
                        rs.getInt("deliveryID"),
                        rs.getInt("invoiceID"),
                        rs.getString("address"),
                        rs.getDate("deliveryDate").toLocalDate(),
                        rs.getString("status")
                ));
            }
        }
        return list;
    }

    public List<Delivery> searchDeliveries(String invoiceSearch, String statusSearch) throws Exception {
        List<Delivery> list = new ArrayList<>();
        String sql = "SELECT deliveryID, invoiceID, address, deliveryDate, status FROM tblDeliveries WHERE 1=1";

        if (invoiceSearch != null && !invoiceSearch.trim().isEmpty()) {
            sql += " AND invoiceID = ?";
        }
        if (statusSearch != null && !statusSearch.trim().isEmpty()) {
            sql += " AND status = ?";
        }

        sql += " ORDER BY deliveryID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {

            int paramIndex = 1;

            if (invoiceSearch != null && !invoiceSearch.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(invoiceSearch));
            }
            if (statusSearch != null && !statusSearch.trim().isEmpty()) {
                ps.setString(paramIndex++, statusSearch);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Delivery d = new Delivery();
                d.setDeliveryID(rs.getInt("deliveryID"));
                d.setInvoiceID(rs.getInt("invoiceID"));
                d.setAddress(rs.getString("address"));
                d.setDeliveryDate(rs.getDate("deliveryDate").toLocalDate());
                d.setStatus(rs.getString("status"));
                list.add(d);
            }
        }

        return list;
    }

    public boolean updateStatus(int deliveryID, String status) throws Exception {
        String updateStatusDelivery = "UPDATE tblDeliveries SET status = ? WHERE deliveryID = ?";
        String getInvoiceId = "SELECT invoiceID FROM tblDeliveries WHERE deliveryID = ?";
        String updateStatusInvoice = "UPDATE tblInvoices SET status = ? WHERE invoiceID = ?";

        try ( Connection con = DBUtils.getConnection()) {
            // Bắt đầu transaction
            con.setAutoCommit(false);

            try ( PreparedStatement psUpdateDelivery = con.prepareStatement(updateStatusDelivery);  PreparedStatement psGetInvoiceId = con.prepareStatement(getInvoiceId);  PreparedStatement psUpdateInvoice = con.prepareStatement(updateStatusInvoice)) {
                // Cập nhật trạng thái giao hàng
                psUpdateDelivery.setString(1, status);
                psUpdateDelivery.setInt(2, deliveryID);
                int rowsAffected = psUpdateDelivery.executeUpdate();

                if (rowsAffected == 0) {
                    con.rollback();
                    return false;
                }

                // Lấy invoiceID tương ứng
                psGetInvoiceId.setInt(1, deliveryID);
                ResultSet rs = psGetInvoiceId.executeQuery();

                if (rs.next()) {
                    int invoiceID = rs.getInt("invoiceID");

                    // Cập nhật trạng thái hóa đơn
                    psUpdateInvoice.setString(1, status);
                    psUpdateInvoice.setInt(2, invoiceID);
                    psUpdateInvoice.executeUpdate();
                }

                // Commit transaction nếu mọi thứ OK
                con.commit();
                return true;

            } catch (Exception ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
