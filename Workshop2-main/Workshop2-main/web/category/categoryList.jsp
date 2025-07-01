<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.Category" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Category List Page</title>
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
        <c:if test=" ${empty sessionScope.LOGIN_USER || 
                       (sessionScope.LOGIN_USER.roleID ne 'AD' && 
                       sessionScope.LOGIN_USER.roleID ne 'SE' && 
                       sessionScope.LOGIN_USER.roleID ne 'MK' && 
                       sessionScope.LOGIN_USER.roleID ne 'BU')}">
            <c:redirect url="login.jsp"></c:redirect>
        </c:if>

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
                          <a href="MainController?action=ViewProducts">Product</a>
                    </c:if>
                    <!-- category -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'SE' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'MK'}">
                          <a class="active" href="MainController?action=ViewCategories">Category</a>
                    </c:if>   
                    <!-- cart -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU'}">
                          <a href="MainController?action=ViewCart">Cart</a>
                    </c:if>
                    <!-- invoice -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'AC'}">
                          <a href="MainController?action=ViewInvoice">Invoice</a>
                    </c:if>
                    <!-- delivery -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'DL'}">
                          <a href="MainController?action=ViewDelivery">Delivery</a>   
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
                        <a href="MainController?action=SearchUser">User Management</a>
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
                        <div class="card-header">Search Categories</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="post">
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="cateSearch" placeholder="Search" value="${requestScope.keyword}">
                                </div>
                                <div class="col-md-6">
                                    <button type="submit" name="action" value="ViewCategories" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Create Category Toggle -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                        <div class="mb-4">
                            <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createCategoryForm">
                                Create New Category
                            </button>
                        </div>
                    </c:if>


                    <!-- Create Form -->
                    <div class="collapse mb-4" id="createCategoryForm">
                        <div class="card">
                            <div class="card-header">Create Category</div>
                            <div class="card-body">
                                <form action="MainController" method="POST" class="row g-3">
                                    <div class="col-md-6">
                                        <label for="categoryName" class="form-label">Category Name</label>
                                        <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="description" class="form-label">Description</label>
                                        <input type="text" class="form-control" id="description" name="description" required>
                                    </div>
                                    <div class="col-md-4 d-flex align-items-end">
                                        <button type="submit" name="action" value="CreateCategory" class="btn btn-success w-100">Create</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Category List Table -->
                    <c:if test="${empty list}">
                        <div class="alert alert-warning">No matching categories found!</div>
                    </c:if>

                    <c:if test="${not empty requestScope.list}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>No</th>
                                        <th>Category</th>
                                        <th>Description</th>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                            <th>Actions</th>
                                            </c:if>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'MK'}">
                                            <th>Promotion</th>
                                            </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="category" items="${requestScope.list}" varStatus="st">
                                        <tr>
                                            <td>${st.count}</td>
                                            <td>${category.categoryName}</td>
                                            <td>${category.description}</td>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                                <td>
                                                    <div class="d-flex gap-2">
                                                        <form action="MainController" method="get">
                                                            <input type="hidden" name="action" value="UpdateCategory" />
                                                            <input type="hidden" name="id" value="${category.categoryID}" />
                                                            <button type="submit" class="btn btn-warning btn-sm">Update</button>
                                                        </form>
                                                        <form action="MainController" method="post" onsubmit="return confirm('Are you sure to delete this category?')">
                                                            <input type="hidden" name="id" value="${category.categoryID}" />
                                                            <button type="submit" name="action" value="DeleteCategory" class="btn btn-danger btn-sm">Delete</button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </c:if>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'MK'}">
                                                <td>
                                                    <form action="MainController" method="POST" id="promoForm-${category.categoryID}">
                                                        <input type="hidden" name="categoryID" value="${category.categoryID}">
                                                        <select class="form-select form-select-sm" name="promoID" onchange="if (this.value == '0') {
                                                                    if (confirm('Removing promotion will restore original prices. Continue?'))
                                                                        this.form.submit();
                                                                } else {
                                                                    this.form.submit();
                                                                }">
                                                            <option value="0" ${category.promoID == 0 ? 'selected' : ''}>None</option>
                                                            <c:forEach var="promo" items="${requestScope.PROMOTION_LIST}">
                                                                <option value="${promo.promoID}" ${category.promoID == promo.promoID ? 'selected' : ''}>
                                                                    ${promo.name} (${promo.discountPercent}%)
                                                                </option>
                                                            </c:forEach>
                                                        </select>
                                                        <input type="hidden" name="action" value="ApplyCategoryPromotion">
                                                    </form>
                                                </td>
                                            </c:if>
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