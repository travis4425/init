/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.user;

import dao.UserDAO;
import dto.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
@WebServlet(name="CreateUserController", urlPatterns={"/CreateUserController"})
public class CreateUserController extends HttpServlet {
    
    private UserDAO dao = new UserDAO();
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String userID = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String roleID = request.getParameter("roleID");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        
        System.out.println(userID + " " + fullName + " " + roleID + " " + password + " " + phone);
        User user = new User(userID, fullName, roleID, password, phone);
        try {
            dao.create(user); // thêm phương thức create vào UserDAO
            request.setAttribute("MSG", "User Created Successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("MSG", "User Created Failed!");
        }

        // Load lại danh sách
        request.getRequestDispatcher("SearchUserController").forward(request, response);
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
}
