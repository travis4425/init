<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/pageStyle.css"> 
        <title>User List Page</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.LOGIN_USER || sessionScope.LOGIN_USER.roleID ne 'AD'}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>

        <div class="container">
            <div class="sidebar">
                <h2>Menu</h2>
                <a href="MainController?action=SearchStock">Products List</a>
                <a href="MainController?action=SearchTransaction">Categories List</a>
                <a href="MainController?action=ViewCart">Carts List</a>
                <a href="MainController?action=ViewInvoiceDetail">Invoices List</a>
                <a href="MainController?action=ViewAlerts">Deliveries List</a>
                <a href="MainController?action=ViewAlerts">Customer Care</a>
                <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                    <a class="active" href="MainController?action=SearchUser">User List</a>
                </c:if>

            </div>

            <div class="main-content">
                <div class="header">
                    <h1>Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/></h1>
                    <a href="${pageContext.request.contextPath}/LogoutController" class="logout-link">Logout</a>
                </div>

                <hr>

                <div class="function-header">
                    <div class="function">
                        <!--search form-->
                        <form action="MainController" method="get">
                            User ID: <input type="text" name="SearchUserID" />
                            Full Name: <input type="text" name="SearchFullName"/>
                            Role:
                            <select name="SearchRoleID">
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

                        <!--create form-->
                        <button id="showCreateForm" class="button-green" onclick="toggleCreateForm()">Create</button>
                        <div id="createForm" style="display: none;">
                            <h3>Create New User</h3>
                            <form action="MainController" method="POST">
                                <div class="form-group">
                                    <label for="userID">User ID:</label>
                                    <input type="text" id="userID" name="userID" required>
                                </div>
                                <div class="form-group">
                                    <label for="fullName">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" required>
                                </div>
                                
                                <div class="form-group">
                                    <label for="roleID">Role ID:</label>
                                    <select id="roleID" name="roleID" required>
                                        <option value="AD">Admin</option>
                                        <option value="SE">Seller</option>
                                        <option value="BU">Buyer</option>
                                        <option value="MK">Marketing</option>
                                        <option value="DL">Delivery</option>
                                        <option value="CS">Customer Care</option>
                                    </select>
                                </div> 
                                
                                <div class="form-group">
                                    <label for="password">Password:</label>
                                    <input type="password" id="password" name="password" required>
                                </div>
                                <div class="form-group">
                                    <label for="phone">Phone:</label>
                                    <input type="text" id="phone" name="phone" required>
                                </div>

                                <button type="submit" name="action" value="CreateUser">Create</button>
                            </form>
                        </div>
                    </div>
                            
                    <div class="message">
                        <c:if test="${not empty MSG}">
                            <h3 id="msg" class="msg" style="color: ${MSG.contains('Failed') ? 'red' : 'green'};">
                                  ${MSG}
                            </h3>
                        </c:if>
                    </div>
                </div>
                
                <c:if test="${empty listUser}">
                    <p style="margin: 10px 0 0;">No matching users found!</p>
                </c:if>

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
                                    <form action="MainController" method="get" style="display:inline;">
                                        <input type="hidden" name="action" value="GetUser" />
                                        <input type="hidden" name="userID" value="${user.userID}" />
                                        <button type="submit">Update</button>
                                    </form>
                                    <form action="MainController" method="post" style="display:inline;">
                                        <input type="hidden" name="userID" value="${user.userID}"/>
                                        <button class="butDelete" type="submit" name="action" value="DeleteUser" onclick="return confirm('Are you sure to delete this user?')">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <script>
            function toggleCreateForm() {
                const formDiv = document.getElementById("createForm");
                const btn = document.getElementById("showCreateForm");
                if (formDiv.style.display === "none") {
                    formDiv.style.display = "block";
                    btn.classList.remove("button-green");
                    btn.classList.add("button-red");
                    btn.innerHTML = "Close";
                } else {
                    formDiv.style.display = "none";
                    btn.classList.remove("button-red");
                    btn.classList.add("button-green");
                    btn.innerHTML = "Create";
                }
            }

            window.addEventListener("DOMContentLoaded", () => {
                const msg = document.getElementById("msg");
                if (msg) {
                    setTimeout(() => {
                        msg.style.opacity = "0"; // mờ dần
                        setTimeout(() => {
                            msg.style.display = "none"; // ẩn hoàn toàn sau khi mờ
                        }, 500); // delay đúng bằng transition ở CSS (0.5s)
                    }, 3000); // 3 giây trước khi bắt đầu mờ
                }
            });
        </script>

    </body>
</html>