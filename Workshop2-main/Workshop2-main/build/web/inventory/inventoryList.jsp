<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.Category" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Inventory Management</title>
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
            .sidebar a:hover, .sidebar .active {
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
        <c:if test="${empty sessionScope.LOGIN_USER || (sessionScope.LOGIN_USER.roleID ne 'AD' && sessionScope.LOGIN_USER.roleID ne 'SE')}">
            <c:redirect url="login.jsp"/>
        </c:if>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-2 sidebar py-4">
                    <h4 class="text-center mb-4">Menu</h4>
                    <a href="MainController?action=ViewProducts">Product</a>
                    <a href="MainController?action=ViewCategories">Category</a>
                    <a href="MainController?action=ViewCart">Cart</a>
                    <a href="MainController?action=ViewInvoice">Invoice</a>
                    <a href="MainController?action=ViewDelivery">Delivery</a>
                    <a href="MainController?action=ViewReturn">Return</a>
                    <a href="PromotionController">Promotion</a>
                    <a href="MainController?action=CustomerCare">Customer Care</a>
                    <a href="MainController?action=Inventory">Inventory</a>
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                        <a href="MainController?action=SearchUser">User Management</a>
                    </c:if>
                    
                </div>
                <div class="col-md-10 p-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h2>Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/></h2>
                        <a href="LogoutController" class="btn btn-danger">Logout</a>
                    </div>

                    <c:if test="${not empty MSG}">
                        <div id="msg" class="alert alert-fixed alert-${MSG.contains('Failed') ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                            ${MSG}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <div class="card mb-4">
                        <div class="card-header">Search Inventory</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="get">
                                <input type="hidden" name="action" value="Inventory"/>
                                <div class="col-md-3">
                                    <input type="number" class="form-control" name="warehouseSearch" placeholder="Warehouse ID" value="${warehouseSearch}"/>
                                </div>
                                <div class="col-md-3">
                                    <input type="number" class="form-control" name="productSearch" placeholder="Product ID" value="${productSearch}"/>
                                </div>
                                <div class="col-md-3">
                                    <button type="submit" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="mb-4">
                        <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createForm">Create New Inventory</button>
                    </div>

                    <div class="collapse mb-4" id="createForm">
                        <div class="card">
                            <div class="card-header">Create Inventory</div>
                            <div class="card-body">
                                <form action="MainController" method="post" class="row g-3">
                                    <input type="hidden" name="action" value="CreateInventory" />
                                    <div class="col-md-3">
                                        <input type="number" class="form-control" name="warehouseID" placeholder="Warehouse ID" required/>
                                    </div>
                                    <div class="col-md-3">
                                        <input type="number" class="form-control" name="productID" placeholder="Product ID" required/>
                                    </div>
                                    <div class="col-md-3">
                                        <input type="number" class="form-control" name="stockQuantity" placeholder="Quantity" required/>
                                    </div>
                                    <div class="col-md-3 d-flex align-items-end">
                                        <button type="submit" class="btn btn-success w-100">Create</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <c:if test="${empty LIST}">
                        <div class="alert alert-warning">No inventory records found.</div>
                    </c:if>

                    <c:if test="${not empty LIST}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>Warehouse ID</th>
                                        <th>Product ID</th>
                                        <th>Quantity</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="inv" items="${LIST}">
                                        <tr>
                                            <td>${inv.warehouseID}</td>
                                            <td>${inv.productID}</td>
                                            <td>
                                                <form action="MainController" method="post" class="d-flex gap-2">
                                                    <input type="hidden" name="action" value="UpdateInventory" />
                                                    <input type="hidden" name="warehouseID" value="${inv.warehouseID}" />
                                                    <input type="hidden" name="productID" value="${inv.productID}" />
                                                    <input type="number" name="stockQuantity" class="form-control form-control-sm" value="${inv.stockQuantity}" />
                                                    <button class="btn btn-sm btn-primary" type="submit">Update</button>
                                                </form>
                                            </td>
                                            <td>
                                                <form action="MainController" method="post" onsubmit="return confirm('Delete this inventory?');">
                                                    <input type="hidden" name="action" value="DeleteInventory" />
                                                    <input type="hidden" name="warehouseID" value="${inv.warehouseID}" />
                                                    <input type="hidden" name="productID" value="${inv.productID}" />
                                                    <button class="btn btn-danger btn-sm">Delete</button>
                                                </form>
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
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                    window.addEventListener("DOMContentLoaded", () => {
                                                        const msg = document.getElementById("msg");
                                                        if (msg) {
                                                            setTimeout(() => {
                                                                msg.classList.remove("show");
                                                                setTimeout(() => msg.remove(), 500);
                                                            }, 3000);
                                                        }
                                                    });
        </script>
    </body>
</html>