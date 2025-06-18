
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Update Page</title>
    </head>
    <body>
        <%
            User loginUser = (User) session.getAttribute("LOGIN_USER");
            if (loginUser == null || !"AD".equals(loginUser.getRoleID())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>
        
        <h1>User Update</h1>
        
        <form action="MainController" method="post">
            <input type="hidden" name="action" value="UpdateUser" />
            <input type="hidden" name="userID" value="${user.userID}" />
            Full Name: <input type="text" name="fullName" value="${user.fullName}" required/><br/>
            Role ID:
            <select name="roleID">
                <option value="AD" ${user.roleID == 'AD' ? 'selected' : ''}>Admin</option>
                <option value="SE" ${user.roleID == 'SE' ? 'selected' : ''}>Seller</option>
                <option value="BU" ${user.roleID == 'BU' ? 'selected' : ''}>Buyer</option>
                <option value="MK" ${user.roleID == 'MK' ? 'selected' : ''}>Marketing</option>
                <option value="DL" ${user.roleID == 'DL' ? 'selected' : ''}>Delivery</option>
                <option value="CS" ${user.roleID == 'CS' ? 'selected' : ''}>Customer Care</option>
            </select><br/>
            Password: <input type="password" name="password" value="${user.password}" required/><br/>
            Phone: <input type="text" name="phone" value="${user.phone}" required/><br/>
            <button type="submit">Save</button>
        </form>
        <a href="MainController?action=SearchUser">Back to User List</a>

    </body>
</html>
