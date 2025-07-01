<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.Delivery" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Delivery Management</title>
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
                      sessionScope.LOGIN_USER.roleID ne 'DL' && 
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
                          <a class="active" href="MainController?action=ViewDelivery">Delivery</a>   
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
                        <div class="card-header bg-light">
                            <strong>Search Deliveries</strong>
                        </div>
                        <div class="card-body">
                            <form class="row g-3 mb-4" method="GET" action="MainController">
                                <input type="hidden" name="action" value="SearchDelivery"/>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="invoiceSearch" placeholder="Invoice ID" value="${param.invoiceSearch}">
                                </div>
                                <div class="col-md-4">
                                    <select class="form-select" name="statusSearch">
                                        <option value="">All Status</option>
                                        <option value="Pending" ${param.statusSearch eq 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="Delivering" ${param.statusSearch eq 'Delivering' ? 'selected' : ''}>Delivering</option>
                                        <option value="Delivered" ${param.statusSearch eq 'Delivered' ? 'selected' : ''}>Delivered</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Delivery List -->
                    <c:if test="${empty DELIVERY_LIST}">
                        <div class="alert alert-warning">No Delivery Records Found!</div>
                    </c:if>

                    <c:if test="${not empty DELIVERY_LIST}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>Delivery ID</th>
                                        <th>Invoice ID</th>
                                        <th>Address</th>
                                        <th>Delivery Date</th>
                                        <th>Status</th>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'DL'}">
                                            <th>Action</th>
                                            </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${DELIVERY_LIST}">
                                        <tr>
                                            <td>${item.deliveryID}</td>
                                            <td>${item.invoiceID}</td>
                                            <td>${item.address}</td>
                                            <td>${item.deliveryDate}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.status eq 'Pending'}">
                                                        <span class="badge bg-secondary">Pending</span>
                                                    </c:when>
                                                    <c:when test="${item.status eq 'Delivering'}">
                                                        <span class="badge bg-warning text-dark">Delivering</span>
                                                    </c:when>
                                                    <c:when test="${item.status eq 'Delivered'}">
                                                        <span class="badge bg-success">Delivered</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-dark">${item.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'DL'}">
                                                <td>
                                                    <form action="MainController" method="POST" class="d-flex gap-2">
                                                        <input type="hidden" name="action" value="UpdateStatusDelivery">
                                                        <input type="hidden" name="deliveryID" value="${item.deliveryID}">

                                                        <button type="submit" name="status" value="Delivering"
                                                                class="btn btn-warning btn-sm"
                                                                ${item.status eq 'Delivered' ? 'disabled' : ''}>
                                                            Delivering
                                                        </button>

                                                        <button type="submit" name="status" value="Delivered"
                                                                class="btn btn-success btn-sm"
                                                                ${item.status eq 'Delivered' ? 'disabled' : ''}>
                                                            Delivered
                                                        </button>
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
