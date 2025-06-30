<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dto.User" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Return List</title>
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
            <c:when test="${empty sessionScope.LOGIN_USER}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>

        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-2 sidebar py-4">
                    <h4 class="text-center mb-4">Menu</h4>
                    <!<!-- product -->
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
                        <a href="MainController?action=ViewDelivery">Delivery</a>   
                    </c:if>
                    <!-- return -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU'}">
                        <a class="active" href="MainController?action=ViewReturn">Return</a>  
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

                    <c:if test="${not empty MSG}">
                        <div id="msg" class="alert alert-fixed alert-${MSG.contains('Fail') ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                            ${MSG}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Search Form -->
                    <div class="card mb-4">
                        <div class="card-header">Search Return Order</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="GET">
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="invoiceSearch" placeholder="Invoice ID" value="${param.invoiceSearch}">
                                </div>
                                <div class="col-md-4">
                                    <select class="form-select" name="statusSearch">
                                        <option value="">All Status</option>
                                        <option value="Pending" ${param.statusSearch eq 'Pending' ? 'selected' : ''}>Pending</option>
                                        <option value="Approved" ${param.statusSearch eq 'Approved' ? 'selected' : ''}>Approved</option>
                                        <option value="Rejected" ${param.statusSearch eq 'Rejected' ? 'selected' : ''}>Rejected</option>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <button type="submit" name="action" value="ViewReturn" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Return List Table -->
                    <c:if test="${empty returnList}">
                        <div class="alert alert-warning">Don't have any request return order.</div>
                    </c:if>

                    <c:if test="${not empty returnList}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-info">
                                    <tr>
                                        <th>Return ID</th>
                                        <th>Invoice ID</th>
                                        <th>Reason</th>
                                        <th>Status</th>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                            <th>Action</th>
                                        </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="r" items="${returnList}">
                                        <tr>
                                            <td>${r.returnID}</td>
                                            <td>${r.invoiceID}</td>
                                            <td>${r.reason}</td>
                                            <td>${r.status}</td>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                                <td>
                                                    <form action="MainController" method="POST" class="d-flex gap-1">
                                                        <input type="hidden" name="action" value="UpdateStatusReturn">
                                                        <input type="hidden" name="returnID" value="${r.returnID}">
                                                        <button class="btn btn-success btn-sm" name="status" value="Approve" ${r.status eq 'Pending' ? '' : 'disabled'}>Approve</button>
                                                        <button class="btn btn-danger btn-sm" name="status" value="Rejected" ${r.status eq 'Pending' ? '' : 'disabled'}>Reject</button>
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
                        msg.classList.remove("show");
                        setTimeout(() => msg.remove(), 500);
                    }, 3000);
                }
            });
        </script>
    </body>
</html>
