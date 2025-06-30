/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dao;

import dto.CustomerCare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CustomerCareDAO {

    // Tạo mới yêu cầu CSKH
    public boolean createCustomerCare(String userID, String subject, String content) throws Exception {
        boolean check = false;
        Connection conn = DBUtils.getConnection();
        String sql = "INSERT INTO tblCustomerCares(userID, subject, content, status) VALUES (?, ?, ?, 'Pending')";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, userID);
        stm.setString(2, subject);
        stm.setString(3, content);
        check = stm.executeUpdate() > 0;
        conn.close();
        return check;
    }

    // Lấy danh sách ticket, có lọc theo subject hoặc content
    public List<CustomerCare> getCustomerCareList(String search) throws Exception {
        List<CustomerCare> list = new ArrayList<>();
        Connection conn = DBUtils.getConnection();
        String sql = "SELECT ticketID, userID, subject, content, status, reply "
                   + "FROM tblCustomerCares "
                   + "WHERE subject LIKE ? OR content LIKE ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, "%" + search + "%");
        stm.setString(2, "%" + search + "%");
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            CustomerCare dto = new CustomerCare();
            dto.setTicketID(rs.getInt("ticketID"));
            dto.setUserID(rs.getString("userID")); //dùng getString()
            dto.setSubject(rs.getString("subject"));
            dto.setContent(rs.getString("content"));
            dto.setStatus(rs.getString("status"));
            dto.setReply(rs.getString("reply"));
            list.add(dto);
        }
        conn.close();
        return list;
    }
    
    
    public List<CustomerCare> getCustomerCareByUser(String userID) throws Exception {
        List<CustomerCare> list = new ArrayList<>();
        Connection conn = DBUtils.getConnection();
        String sql = "SELECT ticketID, userID, subject, content, status, reply "
                   + "FROM tblCustomerCares WHERE userID = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, userID);
        ResultSet rs = stm.executeQuery();
        while (rs.next()) {
            CustomerCare dto = new CustomerCare();
            dto.setTicketID(rs.getInt("ticketID"));
            dto.setUserID(rs.getString("userID"));
            dto.setSubject(rs.getString("subject"));
            dto.setContent(rs.getString("content"));
            dto.setStatus(rs.getString("status"));
            dto.setReply(rs.getString("reply"));
            list.add(dto);
        }
        conn.close();
        return list;
    }
    

    // Trả lời yêu cầu CSKH
    public boolean replyCustomerCare(int ticketID, String reply) throws Exception {
        boolean check = false;
        Connection conn = DBUtils.getConnection();
        String sql = "UPDATE tblCustomerCares SET reply = ?, status = 'replied' WHERE ticketID = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setString(1, reply);
        stm.setInt(2, ticketID);
        check = stm.executeUpdate() > 0;
        conn.close();
        return check;
    }

    // Xóa yêu cầu CSKH
    public boolean deleteCustomerCare(int ticketID) throws Exception {
        boolean check = false;
        Connection conn = DBUtils.getConnection();
        String sql = "DELETE FROM tblCustomerCares WHERE ticketID = ?";
        PreparedStatement stm = conn.prepareStatement(sql);
        stm.setInt(1, ticketID);
        check = stm.executeUpdate() > 0;
        conn.close();
        return check;
    }
    
    public boolean createTicket(CustomerCare dto) throws Exception {
    boolean check = false;
    Connection conn = DBUtils.getConnection();
    String sql = "INSERT INTO tblCustomerCares(userID, subject, content, status) VALUES (?, ?, ?, ?)";
    PreparedStatement stm = conn.prepareStatement(sql);
    stm.setString(1, dto.getUserID());      // userID → String
    stm.setString(2, dto.getSubject());
    stm.setString(3, dto.getContent());
    stm.setString(4, dto.getStatus());      // Ví dụ 'pending'
    check = stm.executeUpdate() > 0;
    conn.close();
    return check;
}

}
