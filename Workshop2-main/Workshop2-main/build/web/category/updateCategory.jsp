<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.User"%>
<%@page import="dto.Category"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Category</title>
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
                            <h4 class="mb-0">Update Category</h4>
                        </div>
                        <div class="card-body">
                            <%
                                Category category = (Category) request.getAttribute("CATEGORY");
                                if (category == null) {
                                    response.sendRedirect("categoryList.jsp");
                                    return;
                                }
                            %>
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="UpdateCategory" />
                                <input type="hidden" name="id" value="<%= category.getCategoryID() %>" />

                                <div class="mb-3">
                                    <label for="categoryName" class="form-label">Category Name</label>
                                    <input type="text" id="categoryName" name="categoryName" class="form-control" value="<%= category.getCategoryName() %>" required>
                                </div>

                                <div class="mb-3">
                                    <label for="description" class="form-label">Description</label>
                                    <input type="text" id="description" name="description" class="form-control" value="<%= category.getDescription() %>" required>
                                </div>

                                <%
                                    String MSG = (String) request.getAttribute("MSG");
                                    if (MSG != null) {
                                %>
                                    <div class="alert alert-<%= MSG.contains("successfully") || MSG.contains("Successfully") ? "success" : "danger" %>" role="alert">
                                        <%= MSG %>
                                    </div>
                                <% } %>

                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success">Save Changes</button>
                                    <a href="MainController?action=ViewCategories" class="btn btn-secondary">Back to Category List</a>
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