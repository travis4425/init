<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Promotion Management</title>
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
                    <a class="active" href="PromotionController">Promotion</a>
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
                <div class="card-header">Search Promotions</div>
                <div class="card-body">
                    <form class="row g-3" action="PromotionController" method="get">
                        <div class="col-md-6">
                            <input type="text" class="form-control" name="nameSearch" placeholder="Search" value="${requestScope.nameSearch}">
                        </div>
                        <div class="col-md-6">
                            <button type="submit" name="action" value="search" class="btn btn-primary w-100">Search</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Button Toggle for Create Promotion Form -->
            <c:if test="${sessionScope.LOGIN_USER.roleID == 'MK'}">
                <div class="mb-3">
                    <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createPromoForm">
                        Add Promotion
                    </button>
                </div>

            <!-- Create Promotion Form -->
                <div class="collapse mb-4" id="createPromoForm">
                    <div class="card">
                        <div class="card-header">Create New Promotion</div>
                        <div class="card-body">
                            <form action="PromotionController" method="POST" class="row g-3">
                                <input type="hidden" name="action" value="add"/>
                                    <div class="col-md-6">
                                        <label class="form-label">Promotion Name</label>
                                        <input type="text" class="form-control" name="name" required/>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Discount (%)</label>
                                        <input type="number" class="form-control" name="discountPercent" min="0" max="100" required/>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label">Status</label>
                                        <select name="status" class="form-select">
                                            <option value="true">Active</option>
                                            <option value="false">Inactive</option>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Start Date</label>
                                        <input type="date" class="form-control" name="startDate" required/>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">End Date</label>
                                        <input type="date" class="form-control" name="endDate" required/>
                                    </div>
                                    <div class="col-12 d-flex justify-content-end">
                                        <button type="submit" class="btn btn-success">Add</button>
                                    </div>
                            </form>
                        </div>
                        </div>
                    </div>
                </c:if>

                <!-- Promotion List Table -->
                <h3>Promotion List</h3>

                <c:if test="${empty PROMOTION_LIST}">
                    <div class="alert alert-warning">No promotions found!</div>
                </c:if>

                <c:if test="${not empty PROMOTION_LIST}">
                    <div class="table-responsive">
                        <table class="table table-bordered table-hover align-middle">
                            <thead class="table-primary">
                                <tr>
                                    <th>No</th>
                                    <th>Name</th>
                                    <th>Discount (%)</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <c:if test="${sessionScope.LOGIN_USER.roleID == 'MK'}">
                                        <th>Action</th>
                                    </c:if>
                                    
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${PROMOTION_LIST}" varStatus="st">
                                    <tr>
                                        <td>${st.count}</td>
                                        <td>${item.name}</td>
                                        <td>${item.discountPercent}</td>
                                        <td>${item.startDate}</td>
                                        <td>${item.endDate}</td>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID == 'MK'}">
                                            <td>
                                                <form action="PromotionController" method="POST" onsubmit="return confirm('Are you sure to delete this promotion?')" style="display:inline;">
                                                    <input type="hidden" name="action" value="delete"/>
                                                    <input type="hidden" name="promoID" value="${item.promoID}"/>
                                                    <button type="submit" class="btn btn-danger btn-sm">Delete</button>
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
                setTimeout(() => msg.classList.remove("show"), 3000);
                setTimeout(() => msg.remove(), 3500);
            }
        });
    </script>
</body>
</html>
