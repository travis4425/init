/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.CustomerCare;

import dao.CustomerCareDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "DeleteCustomerCareController", urlPatterns = {"/DeleteCustomerCareController"})
public class DeleteCustomerCareController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int ticketID = Integer.parseInt(request.getParameter("ticketID"));

            CustomerCareDAO dao = new CustomerCareDAO();
            boolean result = dao.deleteCustomerCare(ticketID);

            request.setAttribute("MSG", result ? "Deleted successfully!" : "Failed to delete!");
        } catch (Exception e) {
            log("ERROR at DeleteCustomerCareController: " + e.toString());
        } finally {
            request.getRequestDispatcher("SearchCustomerCareController").forward(request, response);
        }
    }

    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);
}

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { processRequest(request, response); }
}
