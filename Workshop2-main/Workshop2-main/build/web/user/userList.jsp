<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.User" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User List Page</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .sidebar {
                min-height: 100vh;
                background-color: #343a40;
                color: white;
            }
            .sidebar a {
                color: #adb5bd;
                text-decoration: none;
                display: block;
                padding: 10px 15px;
            }
            .sidebar a:hover,
            .sidebar .active {
                background-color: #495057;
                color: #fff;
            }
            .alert-fixed {
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 1050;
                min-width: 250px;
            }
        </style>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.LOGIN_USER || sessionScope.LOGIN_USER.roleID ne 'AD'}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>

        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-2 sidebar py-4">
                    <h4 class="text-center mb-4">Admin Menu</h4>
                    <!-- product -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' ||   
                                  sessionScope.LOGIN_USER.roleID eq 'SE' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'MK'}">
                          <a href="MainController?action=ViewProducts">Product List</a>
                    </c:if>
                    <!-- category -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'SE' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'MK'}">
                          <a href="MainController?action=ViewCategories">Category List</a>
                    </c:if>   
                    <!-- cart -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU'}">
                          <a href="MainController?action=ViewCart">Cart List</a>
                    </c:if>
                    <!-- invoice -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'AC'}">
                          <a href="MainController?action=ViewInvoice">Invoice List</a>
                    </c:if>
                    <!-- delivery -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'DL'}">
                          <a href="DeliveryController">Delivery</a>   
                    </c:if>
                    <!-- return -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU'}">
                          <a href="MainController?action=ViewReturn">Return</a>  
                    </c:if>
                    <!-- promotion -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'MK' ||
                                  sessionScope.LOGIN_USER.roleID eq 'BU'}">
                          <a href="PromotionController">Promotion</a>
                    </c:if>
                    <!-- customer care -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'CS'}">
                          <a href="MainController?action=CustomerCare">Customer Care</a>
                    </c:if>
                    <!-- inventory management -->      
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'CS'}">
                          <a href="MainController?action=Inventory">Inventory Management</a>
                    </c:if>
                    <!-- user -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                        <a class="active" href="MainController?action=SearchUser">User List</a>
                    </c:if>
                </div>

                <!-- Main Content -->
                <div class="col-md-10 p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2>Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/></h2>
                        <a href="${pageContext.request.contextPath}/LogoutController" class="btn btn-danger">Logout</a>
                    </div>

                    <!-- Message -->
                    <c:if test="${not empty MSG}">
                        <div id="msg" class="alert alert-fixed alert-${MSG.contains('Failed') ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                            ${MSG}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Search Form -->
                    <div class="card mb-4">
                        <div class="card-header">Search Users</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="get">
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="SearchUserID" placeholder="User ID" value="${param.SearchUserID}">
                                </div>
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="SearchFullName" placeholder="Full Name" value="${param.SearchFullName}">
                                </div>
                                <div class="col-md-3">
                                    <select class="form-select" name="SearchRoleID">
                                        <option value="" ${empty param.SearchRoleID ? "selected" : ""}>All Roles</option>
                                        <option value="AD" ${param.SearchRoleID eq 'AD' ? "selected" : ""}>Admin</option>
                                        <option value="SE" ${param.SearchRoleID eq 'SE' ? "selected" : ""}>Seller</option>
                                        <option value="BU" ${param.SearchRoleID eq 'BU' ? "selected" : ""}>Buyer</option>
                                        <option value="MK" ${param.SearchRoleID eq 'MK' ? "selected" : ""}>Marketing</option>
                                        <option value="DL" ${param.SearchRoleID eq 'DL' ? "selected" : ""}>Delivery</option>
                                        <option value="CS" ${param.SearchRoleID eq 'CS' ? "selected" : ""}>Customer Care</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <button type="submit" name="action" value="SearchUser" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Create User Toggle -->
                    <div class="mb-4">
                        <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createUserForm">
                            Create New User
                        </button>
                    </div>

                    <!-- Create Form -->
                    <div class="collapse mb-4" id="createUserForm">
                        <div class="card">
                            <div class="card-header">Create User</div>
                            <div class="card-body">
                                <form action="MainController" method="POST" class="row g-3">
                                    <div class="col-md-4">
                                        <label for="userID" class="form-label">User ID</label>
                                        <input type="text" class="form-control" id="userID" name="userID" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="fullName" class="form-label">Full Name</label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="roleID" class="form-label">Role ID</label>
                                        <select class="form-select" id="roleID" name="roleID" required>
                                            <option value="AD">Admin</option>
                                            <option value="SE">Seller</option>
                                            <option value="BU">Buyer</option>
                                            <option value="MK">Marketing</option>
                                            <option value="DL">Delivery</option>
                                            <option value="CS">Customer Care</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="password" class="form-label">Password</label>
                                        <input type="password" class="form-control" id="password" name="password" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="phone" class="form-label">Phone</label>
                                        <input type="text" class="form-control" id="phone" name="phone" required>
                                    </div>
                                    <div class="col-md-4 d-flex align-items-end">
                                        <button type="submit" name="action" value="CreateUser" class="btn btn-success w-100">Create</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- User List Table -->
                    <c:if test="${empty listUser}">
                        <div class="alert alert-warning">No matching users found!</div>
                    </c:if>

                    <c:if test="${not empty listUser}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>No</th>
                                        <th>User ID</th>
                                        <th>Full Name</th>
                                        <th>Role ID</th>
                                        <th>Password</th>
                                        <th>Phone</th>
                                        <th>Actions</th>
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
                                            <td>
                                                <div class="d-flex gap-2">
                                                    <form action="MainController" method="get">
                                                        <input type="hidden" name="action" value="GetUser" />
                                                        <input type="hidden" name="userID" value="${user.userID}" />
                                                        <button type="submit" class="btn btn-warning btn-sm">Update</button>
                                                    </form>
                                                    <form action="MainController" method="post" onsubmit="return confirm('Are you sure to delete this user?')">
                                                        <input type="hidden" name="userID" value="${user.userID}" />
                                                        <button type="submit" name="action" value="DeleteUser" class="btn btn-danger btn-sm">Delete</button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                        window.addEventListener("DOMContentLoaded", () => {
                                                            const msg = document.getElementById("msg");
                                                            if (msg) {
                                                                setTimeout(() => {
                                                                    msg.classList.remove("show"); // mờ dần
                                                                    setTimeout(() => msg.remove(), 500); // xóa khỏi DOM
                                                                }, 3000);
                                                            }
                                                        });
        </script>
    </body>
</html>
