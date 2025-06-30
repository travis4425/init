<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.Product" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product List Page</title>
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
        <c:if test="${empty sessionScope.LOGIN_USER || 
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
                        <a class="active" href="MainController?action=ViewProducts">Product</a>
                    </c:if>
                    <!-- category -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'SE' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'MK'}">
                        <a href="MainController?action=ViewCategories">Category</a>
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
                        <div class="card-header">Search Products</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="get">
                                <div class="col-md-3">
                                    <input type="text" class="form-control" name="nameSearch" placeholder="Name" value="${requestScope.nameSearch}"> 
                                </div>

                                <div class="col-md-3">
                                    <select class="form-select" name="cateSearch">
                                        <option value="">Any Categories</option>
                                        <c:forEach var="category" items="${requestScope.categories}">
                                            <option value="${category.categoryName}" ${requestScope.cateSearch == category.categoryName ? 'selected' : ''}>
                                                ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="col-md-3">
                                    <input type="number" class="form-control" name="priceSearch" placeholder="Price" value="${requestScope.priceSearch}"> 
                                </div>
                                
<!--                                <div class="col-md-3">
                                    <select class="form-select" name="statusSearch">
                                        <option value="" ${empty param.SearchStatus ? "selected" : ""}>Any Status</option>
                                        <option value="Active" ${param.SearchStatus eq 'Active' ? "selected" : ""}>Active</option>
                                        <option value="Inactive" ${param.SearchStatus eq 'Inactive' ? "selected" : ""}>Inactive</option>
                                    </select>
                                </div>-->
                                <div class="col-md-3">
                                    <button type="submit" name="action" value="ViewProducts" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Create Product Toggle -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID == 'SE'}">
                        <div class="mb-4">
                            <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createProductForm">
                                Create New Product
                            </button>
                        </div>
                    </c:if>
                    

                    <!-- Create Form -->
                    <div class="collapse mb-4" id="createProductForm">
                        <div class="card">
                            <div class="card-header">Create Product</div>
                            <div class="card-body">
                                <form action="MainController" method="POST" class="row g-3">
                                    <div class="col-md-4">
                                        <label for="name" class="form-label">Name</label>
                                        <input type="text" class="form-control" id="name" name="name" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="category" class="form-label">Category</label>
                                        <select class="form-select" id="category" name="categoryID" required>
                                            <option value="">Select Category</option>
                                            <c:forEach var="category" items="${requestScope.categories}">
                                                <option value="${category.categoryID}">${category.categoryName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="price" class="form-label">Price</label>
                                        <input type="number" class="form-control" id="price" name="price" step="0.01" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="quantity" class="form-label">Quantity</label>
                                        <input type="number" class="form-control" id="quantity" name="quantity" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="status" class="form-label">Status</label>
                                        <select class="form-select" id="status" name="status" required>
                                            <option value="Active">Active</option>
                                            <option value="Inactive">Inactive</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4 d-flex align-items-end">
                                        <button type="submit" name="action" value="CreateProduct" class="btn btn-success w-100">Create</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Product List Table -->
                    <c:if test="${empty list}">
                        <div class="alert alert-warning">No matching products found!</div>
                    </c:if>

                    <c:if test="${not empty list}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>No</th>
                                        <th>Name</th>
                                        <th>Category</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID == 'AD' || sessionScope.LOGIN_USER.roleID == 'BU'}">
                                            <th>Seller</th>
                                        </c:if>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID == 'SE' || sessionScope.LOGIN_USER.roleID == 'BU'}">
                                            <th>Function</th>
                                        </c:if>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID == 'MK'}">
                                            <th>Promotion</th>
                                        </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="product" items="${list}" varStatus="st">
                                        <tr>
                                            <td>${st.count}</td>
                                            <td>${product.name}</td>
                                            <td><c:out value="${product.cateName}"/></td>
                                            <td><c:out value="${product.price}"/></td>
                                            <td><c:out value="${product.quantity}"/></td>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID == 'AD' || sessionScope.LOGIN_USER.roleID == 'BU'}">
                                                <td><c:out value="${not empty product.sellerFullName ? product.sellerFullName : 'Unknown'}"/></td>
                                            </c:if>
                                                
                                            <c:if test="${sessionScope.LOGIN_USER.roleID == 'SE'}">
                                                <td>
                                                    <div class="d-flex gap-2">
                                                        <form action="MainController" method="get">
                                                            <input type="hidden" name="id" value="${product.productID}">
                                                            <button type="submit" name="action" value="UpdateProduct" class="btn btn-warning btn-sm">Update</button>
                                                        </form>
                                                        <form action="MainController" method="POST" onsubmit="return confirm('Are you sure to delete this product?')>
                                                            <input type="hidden" name="id" value="${product.productID}">
                                                            <button class="btn btn-danger btn-sm" type="submit" name="action" value="DeleteProduct">Delete</button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </c:if>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID == 'BU'}">
                                                <td>
                                                    <div >
                                                        <form action="MainController" method="get">
                                                            <input type="hidden" name="id" value="${product.productID}">
                                                            <button type="submit" name="action" value="AddToCart" class="btn btn-warning btn-sm">Add to Cart</button>
                                                        </form>                                    <%-- sửa chỗ này --%>
                                                    </div>
                                                </td>
                                            </c:if>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID == 'MK'}">
                                                <td>
                                                    <form action="MainController" method="POST" id="promoForm-${product.productID}">
                                                        <input type="hidden" name="productID" value="${product.productID}">
                                                        <select class="form-select form-select-sm" name="promoID" onchange="if(confirm('Apply this promotion?')) this.form.submit()">
                                                            <option value="0" ${empty product.promoID ? 'selected' : ''}>None</option>
                                                            <c:forEach var="promo" items="${requestScope.PROMOTION_LIST}">
                                                                <option value="${promo.promoID}" ${product.promoID == promo.promoID ? 'selected' : ''}>
                                                                    ${promo.name} (${promo.discountPercent}%)
                                                                </option>
                                                            </c:forEach>
                                                        </select>
                                                        <input type="hidden" name="action" value="ApplyPromotion">
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