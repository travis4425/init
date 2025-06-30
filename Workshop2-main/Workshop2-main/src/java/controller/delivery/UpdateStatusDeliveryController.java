/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.delivery;

import dao.DeliveryDAO;
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
@WebServlet(name="UpdateStatusDeliveryController", urlPatterns={"/UpdateStatusDeliveryController"})
public class UpdateStatusDeliveryController extends HttpServlet {
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("LOGIN_USER");

        if (loginUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String idStr = request.getParameter("deliveryID");
        String status = request.getParameter("status");
        
        DeliveryDAO dao = new DeliveryDAO();
        try {
            int id = Integer.parseInt(idStr);
            dao.updateStatus(id, status);
            request.setAttribute("MSG", "Update Delivery Successfully!");
            request.getRequestDispatcher("ViewDeliveryController").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("MSG", "Update Delivery Successfully!");
            request.getRequestDispatcher("ViewDeliveryController").forward(request, response);
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
