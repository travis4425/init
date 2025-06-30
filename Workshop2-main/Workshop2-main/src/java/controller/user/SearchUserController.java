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
import java.util.List;

/**
 *
 * @author Admin
 */
@WebServlet(name="SearchUserController", urlPatterns={"/SearchUserController"})
public class SearchUserController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String userID = request.getParameter("SearchUserID");
        String fullName = request.getParameter("SearchFullName");
        String roleID = request.getParameter("SearchRoleID");
        
        try {
            UserDAO dao = new UserDAO();
            List<User> list = dao.search(userID, fullName, roleID);
            request.setAttribute("listUser", list);
        } catch (Exception e) {
            log("Error at SearchUserController: " + e.toString());
        }
        request.getRequestDispatcher("user/userList.jsp").forward(request, response);
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
