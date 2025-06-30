package dao;

import dto.CartDetail;
import dto.Invoice;
import dto.InvoiceDetail;
import dto.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import utils.DBUtils;

public class InvoiceDAO {

    public int createInvoice(String userID, List<CartDetail> items) throws Exception {
        int invoiceID = -1;
        String insertInvoice = "INSERT INTO tblInvoices (userID, totalAmount, status, createdDate) VALUES (?, ?, 'Pending', GETDATE())";
        String insertDetail = "INSERT INTO tblInvoiceDetails (invoiceID, productID, quantity, price) VALUES (?, ?, ?, ?)";
        String updateProductQty = "UPDATE tblProducts SET quantity = quantity - ? WHERE productID = ?";

        float total = 0;
        for (CartDetail item : items) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        try ( Connection con = DBUtils.getConnection()) {
            con.setAutoCommit(false);

            try ( PreparedStatement psInvoice = con.prepareStatement(insertInvoice, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psInvoice.setString(1, userID);
                psInvoice.setDouble(2, total);
                psInvoice.executeUpdate();

                try ( ResultSet rs = psInvoice.getGeneratedKeys()) {
                    if (rs.next()) {
                        invoiceID = rs.getInt(1);
                    }
                }
            }

            try ( PreparedStatement psDetail = con.prepareStatement(insertDetail);  PreparedStatement psUpdate = con.prepareStatement(updateProductQty)) {

                for (CartDetail item : items) {
                    int pid = item.getProduct().getProductID();
                    int qty = item.getQuantity();
                    double price = item.getProduct().getPrice();

                    psDetail.setInt(1, invoiceID);
                    psDetail.setInt(2, pid);
                    psDetail.setInt(3, qty);
                    psDetail.setDouble(4, price);
                    psDetail.addBatch();

                    psUpdate.setInt(1, qty);
                    psUpdate.setInt(2, pid);
                    psUpdate.addBatch();
                }

                psDetail.executeBatch();
                psUpdate.executeBatch();
            }

            con.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return invoiceID;
    }

    // Dùng cho các chức năng lấy invoice kèm chi tiết (InvoiceDetail)
    public List<Invoice> getInvoicesByUser(String userID) throws Exception {
        List<Invoice> invoices = new ArrayList<>();

        String invoiceSQL = "SELECT invoiceID, totalAmount, status, createdDate FROM tblInvoices WHERE userID = ? ORDER BY invoiceID DESC";
        String detailSQL = "SELECT d.invoiceID, d.productID, d.quantity, d.price, p.name "
                + "FROM tblInvoiceDetails d "
                + "JOIN tblProducts p ON d.productID = p.productID "
                + "WHERE d.invoiceID = ? "
                + "ORDER BY d.invoiceID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement psInvoice = con.prepareStatement(invoiceSQL)) {

            psInvoice.setString(1, userID);
            ResultSet rsInvoice = psInvoice.executeQuery();

            while (rsInvoice.next()) {
                int invoiceID = rsInvoice.getInt("invoiceID");
                float totalAmount = rsInvoice.getFloat("totalAmount");
                String status = rsInvoice.getString("status");
                LocalDate createdDate = rsInvoice.getDate("createdDate").toLocalDate();

                Invoice invoice = new Invoice(invoiceID, userID, status, totalAmount, createdDate);
                ReturnDAO returnDAO = new ReturnDAO(); // gọi ở đầu hàm
                invoice.setReturned(returnDAO.isReturned(invoice.getInvoiceID()));

                List<InvoiceDetail> details = new ArrayList<>();
                try ( PreparedStatement psDetail = con.prepareStatement(detailSQL)) {
                    psDetail.setInt(1, invoiceID);
                    ResultSet rsDetail = psDetail.executeQuery();

                    while (rsDetail.next()) {
                        int productID = rsDetail.getInt("productID");
                        String name = rsDetail.getString("name");
                        int quantity = rsDetail.getInt("quantity");
                        float price = rsDetail.getFloat("price");

                        Product product = new Product(productID, name, price);
                        InvoiceDetail detail = new InvoiceDetail(invoiceID, quantity, product, price);

                        details.add(detail);
                    }
                }

                invoice.setInvoiceDetails(details);
                invoices.add(invoice);
            }
        }

        return invoices;
    }

    public List<Invoice> getAllInvoices() throws Exception {
        List<Invoice> invoices = new ArrayList<>();

        String sql = "SELECT i.invoiceID, i.userID, i.totalAmount, i.status, i.createdDate, "
                + "       d.productID, d.quantity, d.price, "
                + "       p.name, p.categoryID, p.price AS productPrice, p.status AS productStatus "
                + "FROM tblInvoices i "
                + "JOIN tblInvoiceDetails d ON i.invoiceID = d.invoiceID "
                + "JOIN tblProducts p ON d.productID = p.productID "
                + "ORDER BY i.invoiceID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            Map<Integer, Invoice> invoiceMap = new LinkedHashMap<>();

            while (rs.next()) {
                int invoiceID = rs.getInt("invoiceID");

                Invoice invoice = invoiceMap.get(invoiceID);
                if (invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceID(invoiceID);
                    invoice.setUserID(rs.getString("userID"));
                    invoice.setTotalAmount(rs.getFloat("totalAmount"));
                    invoice.setStatus(rs.getString("status"));
                    invoice.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                    invoice.setInvoiceDetails(new ArrayList<>());

                    invoiceMap.put(invoiceID, invoice);
                }

                // Tạo Product
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setCategoryID(rs.getInt("categoryID"));
                product.setPrice(rs.getFloat("productPrice"));
                product.setStatus(rs.getString("productStatus"));

                // Tạo chi tiết hóa đơn
                InvoiceDetail detail = new InvoiceDetail();
                detail.setProduct(product);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getFloat("price"));

                invoice.getInvoiceDetails().add(detail);
            }

            invoices.addAll(invoiceMap.values());
        }

        return invoices;
    }

    public boolean cancelInvoice(int invoiceID, String userID) throws Exception {
        String checkSQL = "SELECT status FROM tblInvoices WHERE invoiceID = ? AND userID = ?";
        String deleteDeliveriesSQL = "DELETE FROM tblDeliveries WHERE invoiceID = ?";
        String cancelInvoiceSQL = "UPDATE tblInvoices SET status = 'Canceled' WHERE invoiceID = ?";
        String getDetailsSQL = "SELECT productID, quantity FROM tblInvoiceDetails WHERE invoiceID = ?";
        String updateProductSQL = "UPDATE tblProducts SET quantity = quantity + ? WHERE productID = ?";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement psCheck = con.prepareStatement(checkSQL)) {

            con.setAutoCommit(false);

            // 1. Check status
            psCheck.setInt(1, invoiceID);
            psCheck.setString(2, userID);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                rs.close();
                if ("Delivered".equalsIgnoreCase(status)) {
                    con.rollback(); // Rollback before returning
                    return false;   // Cannot cancel delivered invoice
                }
            } else {
                rs.close();
                con.rollback(); // Rollback before returning
                return false;   // Invoice does not exist for user
            }

            // 2. Get product list from invoice details
            Map<Integer, Integer> productRestores = new HashMap<>();
            try ( PreparedStatement psDetails = con.prepareStatement(getDetailsSQL)) {
                psDetails.setInt(1, invoiceID);
                try ( ResultSet rsDetail = psDetails.executeQuery()) {
                    while (rsDetail.next()) {
                        int productID = rsDetail.getInt("productID");
                        int quantity = rsDetail.getInt("quantity");
                        productRestores.put(productID, quantity);
                    }
                }
            }

            // 3. Restore product quantity
            try ( PreparedStatement psRestore = con.prepareStatement(updateProductSQL)) {
                for (Map.Entry<Integer, Integer> entry : productRestores.entrySet()) {
                    psRestore.setInt(1, entry.getValue());
                    psRestore.setInt(2, entry.getKey());
                    psRestore.addBatch();
                }
                psRestore.executeBatch();
            }

            // 4. Delete deliveries
            try ( PreparedStatement psDeleteDeliveries = con.prepareStatement(deleteDeliveriesSQL)) {
                psDeleteDeliveries.setInt(1, invoiceID);
                psDeleteDeliveries.executeUpdate();
            }

            // 5. Update invoice status
            try ( PreparedStatement psCancelInvoice = con.prepareStatement(cancelInvoiceSQL)) {
                psCancelInvoice.setInt(1, invoiceID);
                psCancelInvoice.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<Invoice> searchAllInvoices(int invoiceID, String statusSearch) throws Exception {
        List<Invoice> invoices = new ArrayList<>();

        String sql = "SELECT i.invoiceID, i.userID, i.totalAmount, i.status, i.createdDate, "
                + "       d.productID, d.quantity, d.price, "
                + "       p.name, p.categoryID, p.price AS productPrice, p.status AS productStatus "
                + "FROM tblInvoices i "
                + "JOIN tblInvoiceDetails d ON i.invoiceID = d.invoiceID "
                + "JOIN tblProducts p ON d.productID = p.productID "
                + "WHERE 1=1 ";

        if (invoiceID > 0) {
            sql += " AND i.invoiceID = ? ";
        }
        if (statusSearch != null && !statusSearch.trim().isEmpty()) {
            sql += " AND i.status = ? ";
        }

        sql += "ORDER BY i.invoiceID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            if (invoiceID > 0) {
                ps.setInt(index++, invoiceID);
            }
            if (statusSearch != null && !statusSearch.trim().isEmpty()) {
                ps.setString(index++, statusSearch.trim());
            }

            ResultSet rs = ps.executeQuery();
            Map<Integer, Invoice> invoiceMap = new LinkedHashMap<>();

            while (rs.next()) {
                int invID = rs.getInt("invoiceID");

                Invoice invoice = invoiceMap.get(invID);
                if (invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceID(invID);
                    invoice.setUserID(rs.getString("userID"));
                    invoice.setTotalAmount(rs.getFloat("totalAmount"));
                    invoice.setStatus(rs.getString("status"));
                    invoice.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                    invoice.setInvoiceDetails(new ArrayList<>());

                    invoiceMap.put(invID, invoice);
                }

                // Tạo product và chi tiết hóa đơn
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setCategoryID(rs.getInt("categoryID"));
                product.setPrice(rs.getFloat("productPrice"));
                product.setStatus(rs.getString("productStatus"));

                InvoiceDetail detail = new InvoiceDetail();
                detail.setProduct(product);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getFloat("price"));

                invoice.getInvoiceDetails().add(detail);
            }

            invoices.addAll(invoiceMap.values());
        }

        return invoices;
    }

    public List<Invoice> searchInvoicesByUser(String userID, int invoiceID, String statusSearch) throws Exception {
        List<Invoice> invoices = new ArrayList<>();

        String sql = "SELECT i.invoiceID, i.totalAmount, i.status, i.createdDate, "
                + "       d.productID, d.quantity, d.price, "
                + "       p.name, p.categoryID, p.price AS productPrice, p.status AS productStatus "
                + "FROM tblInvoices i "
                + "JOIN tblInvoiceDetails d ON i.invoiceID = d.invoiceID "
                + "JOIN tblProducts p ON d.productID = p.productID "
                + "WHERE i.userID = ? ";

        if (invoiceID > 0) {
            sql += " AND i.invoiceID = ? ";
        }
        if (statusSearch != null && !statusSearch.trim().isEmpty()) {
            sql += " AND i.status = ? ";
        }

        sql += "ORDER BY i.invoiceID DESC";

        try ( Connection con = DBUtils.getConnection();  PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            ps.setString(index++, userID);

            if (invoiceID > 0) {
                ps.setInt(index++, invoiceID);
            }
            if (statusSearch != null && !statusSearch.trim().isEmpty()) {
                ps.setString(index++, statusSearch.trim());
            }

            ResultSet rs = ps.executeQuery();
            Map<Integer, Invoice> invoiceMap = new LinkedHashMap<>();

            while (rs.next()) {
                int invID = rs.getInt("invoiceID");

                Invoice invoice = invoiceMap.get(invID);
                if (invoice == null) {
                    invoice = new Invoice();
                    invoice.setInvoiceID(invID);
                    invoice.setUserID(userID);
                    invoice.setTotalAmount(rs.getFloat("totalAmount"));
                    invoice.setStatus(rs.getString("status"));
                    invoice.setCreatedDate(rs.getDate("createdDate").toLocalDate());
                    invoice.setInvoiceDetails(new ArrayList<>());

                    ReturnDAO returnDAO = new ReturnDAO();
                    invoice.setReturned(returnDAO.isReturned(invID));

                    invoiceMap.put(invID, invoice);
                }

                // Tạo chi tiết hóa đơn
                Product product = new Product();
                product.setProductID(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setCategoryID(rs.getInt("categoryID"));
                product.setPrice(rs.getFloat("productPrice"));
                product.setStatus(rs.getString("productStatus"));

                InvoiceDetail detail = new InvoiceDetail();
                detail.setProduct(product);
                detail.setQuantity(rs.getInt("quantity"));
                detail.setPrice(rs.getFloat("price"));

                invoice.getInvoiceDetails().add(detail);
            }

            invoices.addAll(invoiceMap.values());
        }

        return invoices;
    }

}
