<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update User</title>
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">

        <!-- Role check -->
        <c:choose>
            <c:when test="${empty sessionScope.LOGIN_USER || sessionScope.LOGIN_USER.roleID ne 'AD'}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>

        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Update User</h4>
                        </div>
                        <div class="card-body">
                            <form action="MainController" method="post">
                                <input type="hidden" name="action" value="UpdateUser" />
                                <input type="hidden" name="userID" value="${user.userID}" />

                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Full Name</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" value="${user.fullName}" required>
                                </div>

                                <div class="mb-3">
                                    <label for="roleID" class="form-label">Role</label>
                                    <select name="roleID" id="roleID" class="form-select" required>
                                        <option value="AD" ${user.roleID == 'AD' ? 'selected' : ''}>Admin</option>
                                        <option value="SE" ${user.roleID == 'SE' ? 'selected' : ''}>Seller</option>
                                        <option value="BU" ${user.roleID == 'BU' ? 'selected' : ''}>Buyer</option>
                                        <option value="MK" ${user.roleID == 'MK' ? 'selected' : ''}>Marketing</option>
                                        <option value="DL" ${user.roleID == 'DL' ? 'selected' : ''}>Delivery</option>
                                        <option value="CS" ${user.roleID == 'CS' ? 'selected' : ''}>Customer Care</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="text" id="password" name="password" class="form-control" value="${user.password}" required>
                                </div>

                                <div class="mb-3">
                                    <label for="phone" class="form-label">Phone</label>
                                    <input type="text" id="phone" name="phone" class="form-control" value="${user.phone}" required>
                                </div>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success">Save Changes</button>
                                    <a href="MainController?action=SearchUser" class="btn btn-secondary">Back to User List</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
