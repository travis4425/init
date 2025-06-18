<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User List Page</title>
    </head>
    <body>
        <%
            String search = request.getParameter("search");
            if (search == null) {
                search = "";
            }
            request.setAttribute("search", search);
            User loginUser = (User) session.getAttribute("LOGIN_USER");
            if (loginUser == null || !"AD".equals(loginUser.getRoleID())) {
                response.sendRedirect("login.jsp");
                return;
            }
        %>
        <h1>User Management</h1>
        
        <a href="${pageContext.request.contextPath}/LogoutController" class="logout-link">Logout</a>
        
        <div>
            <h3>Create New User</h3>
            <form action="MainController" method="POST">
                
                User ID:<input type="text" name="userID" required>
                Full Name:<input type="text" name="fullName" required>

                Role ID:
                <select id="roleID" name="roleID" required>
                    <option value="AD">Admin</option>
                    <option value="SE">Seller</option>
                    <option value="BU">Buyer</option>
                    <option value="MK">Marketing</option>
                    <option value="DL">Delivery</option>
                    <option value="CS">Customer Care</option>
                </select>

                Password:<input type="password" name="password" required>
                Phone:<input type="text" name="phone" required>

                <button type="submit" name="action" value="CreateUser">Create</button>
            </form>
        </div> 
        
        <c:if test="${not empty MSG}">
            <div class="msg" style="color: ${MSG.contains('Failed') ? 'red' : 'green'};">
                ${MSG}
            </div>
        </c:if>

        
        <hr>
        
        <h3>Search Users</h3>
        <form action="MainController" method="get">
            User ID: <input type="text" name="userID" value="${param.userID}" />
            Full Name: <input type="text" name="fullName" value="${param.fullName}" />
            Role:
            <select name="roleID">
                <option value="">All</option>
                <option value="AD">Admin</option>
                <option value="SE">Seller</option>
                <option value="BU">Buyer</option>
                <option value="MK">Marketing</option>
                <option value="DL">Delivery</option>
                <option value="CS">Customer Care</option>
            </select>
            <button type="submit" name="action" value="SearchUser">Search</button>
        </form>
        
        <hr>
            
        <table>
            <thead>
                <tr>
                    <th>No</th><th>User ID</th><th>Full Name</th><th>Role ID</th><th>Password</th><th>Phone</th><th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${listUser}" varStatus="st">
                    <tr>
                        <td>${st.count}</td>
                        <td>${user.userID}</td>
                        <td>${user.fullName}</td>
                        <td>${user.roleID}</td>
                        <td>${user.password}</td>
                        <td>${user.phone}</td>
                        <td class="actions">
                            <a class="btn btn-sm btn-warning" href="MainController?action=GetUser&userID=${user.userID}">Update</a>
                            <form action="MainController" method="post" style="display:inline;">
                                <input type="hidden" name="userID" value="${user.userID}"/>
                                <input type="hidden" name="search" value="${search}"/>
                                <button class="btn btn-sm btn-danger" type="submit" name="action" value="DeleteUser" onclick="return confirm('Are you sure to delete this user?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
