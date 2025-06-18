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

/**
 *
 * @author Admin
 */
@WebServlet(name="RegisterController", urlPatterns={"/RegisterController"})
public class RegisterController extends HttpServlet {
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Chỉ show form đăng ký
        request.getRequestDispatcher("register.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String userID   = request.getParameter("userID");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        String phone    = request.getParameter("phone");
        // role mặc định là Buyer (BU)
        String roleID   = "BU";

        try {
            User user = new User(userID, fullName, roleID, password, phone);
            UserDAO dao = new UserDAO();
            boolean created = dao.create(user);
            if (created) {
                request.setAttribute("MSG", "Register successful! Please login.");
                // sau khi đăng ký thành công, đưa về login
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            } else {
                request.setAttribute("MSG", "Register failed: UserID already exists.");
            }
        } catch (Exception e) {
            log("Error at RegisterUserController: " + e.toString());
            request.setAttribute("MSG", "An error occurred, please try again.");
        }
        // Nếu thất bại, show lại form với message
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }


}
