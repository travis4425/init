/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.Return;

import dao.ReturnDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet(name="ReturnRequestController", urlPatterns={"/ReturnRequestController"})
public class ReturnRequestController extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        int invoiceID = Integer.parseInt(request.getParameter("invoiceID"));
        String reason = request.getParameter("reason");
        ReturnDAO returnDAO = new ReturnDAO();
        
        try {
            returnDAO.createReturn(invoiceID, reason);
            request.setAttribute("MSG", "Đã gửi yêu cầu trả hàng!");
            request.getRequestDispatcher("ViewInvoiceController").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("MSG", "Gửi yêu cầu thất bại!");
            request.getRequestDispatcher("ViewInvoiceController").forward(request, response);
        }
    } 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
