<%-- 
    Document   : updateProduct
    Created on : Jun 18, 2025, 6:51:56 PM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.User"%>
<%@page import="dto.Product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Product</title>
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <!-- Role check -->
        <c:choose>
            <c:when test="${empty sessionScope.LOGIN_USER || sessionScope.LOGIN_USER.roleID ne 'SE'}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>

        <div class="container mt-5">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">Update Product</h4>
                        </div>
                        <div class="card-body">
                            <%
                                Product product = (Product) request.getAttribute("PRODUCT");
                                if (product == null) {
                                    response.sendRedirect("productList.jsp");
                                    return;
                                }
                            %>
                            <form action="MainController" method="POST">
                                <input type="hidden" name="action" value="UpdateProduct" />
                                <input type="hidden" name="id" value="<%= product.getProductID() %>" />

                                <div class="mb-3">
                                    <label for="name" class="form-label">Name</label>
                                    <input type="text" id="name" name="name" class="form-control" value="<%= product.getName() %>" required>
                                </div>

                                <div class="mb-3">
                                    <label for="categoryID" class="form-label">Category</label>
                                    <select name="categoryID" id="categoryID" class="form-select" required>
                                        <option value="">Select Category</option>
                                        <c:forEach var="category" items="${requestScope.categories}">
                                            <option value="${category.categoryID}" ${product.getCategoryID() == category.categoryID ? 'selected' : ''}>
                                                ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="price" class="form-label">Price</label>
                                    <input type="number" id="price" name="price" class="form-control" value="<%= product.getPrice() %>" step="0.01" required>
                                </div>

                                <div class="mb-3">
                                    <label for="quantity" class="form-label">Quantity</label>
                                    <input type="number" id="quantity" name="quantity" class="form-control" value="<%= product.getQuantity() %>" required>
                                </div>

                                <div class="mb-3">
                                    <label for="status" class="form-label">Status</label>
                                    <select name="status" id="status" class="form-select" required>
                                        <option value="Active" <%= "Active".equals(product.getStatus()) ? "selected" : "" %>>Active</option>
                                        <option value="Inactive" <%= "Inactive".equals(product.getStatus()) ? "selected" : "" %>>Inactive</option>
                                    </select>
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
                                    <a href="MainController?action=ViewProducts" class="btn btn-secondary">Back to Product List</a>
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
