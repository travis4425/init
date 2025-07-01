<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Invoice History</title>
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
                          <a class="active" href="MainController?action=ViewInvoice">Invoice</a>
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

                    <!-- Search Invoice -->
                    <div class="card mb-4">
                        <div class="card-header bg-light">
                            <strong>Search Invoice</strong>
                        </div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="GET">
                                <input type="hidden" name="action" value="SearchInvoice"/>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" name="invoiceID" placeholder="Invoice ID" value="${param.invoiceID}">
                                </div>
                                <div class="col-md-4">
                                    <select class="form-select" name="status">
                                        <option value="">All Status</option>
                                        <option value="Processing" ${param.status eq 'Processing' ? 'selected' : ''}>Processing</option>
                                        <option value="Delivering" ${param.status eq 'Delivering' ? 'selected' : ''}>Delivering</option>
                                        <option value="Delivered" ${param.status eq 'Delivered' ? 'selected' : ''}>Delivered</option>
                                        <option value="Canceled" ${param.status eq 'Canceled' ? 'selected' : ''}>Canceled</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <button type="submit" class="btn btn-primary w-100">Search</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <h3 class="mb-3">Invoices History</h3>

                    <c:if test="${empty list}">
                        <div class="alert alert-info">You don't have any invoice.</div>
                    </c:if>

                    <c:forEach var="invoice" items="${list}">
                        <div class="card mb-4 shadow-sm">
                            <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                                <div>
                                    <strong>Invoice #${invoice.invoiceID}</strong> 
                                    | <small>${invoice.createdDate}</small>
                                </div>
                                <span class="badge
                                      ${invoice.status eq 'Delivered' ? 'bg-success' : 
                                        invoice.status eq 'Canceled' ? 'bg-danger' : 
                                        invoice.status eq 'Processing' ? 'bg-warning text-dark' : 
                                        'bg-secondary'}">
                                    <i class="bi ${invoice.status eq 'Delivered' ? 'bi-check-circle-fill' :
                                                   invoice.status eq 'Canceled' ? 'bi-x-circle-fill' :
                                                   'bi-clock-fill'}"></i>
                                       ${invoice.status}
                                    </span>
                                </div>
                                <div class="card-body">
                                    <p><strong>Total:</strong> 
                                        <span class="text-danger fw-bold">
                                            ${invoice.totalAmount} VNĐ
                                        </span>
                                    </p>
                                    <table class="table table-bordered align-middle table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Product ID</th>
                                                <th>Product Name</th>
                                                <th>Quantity</th>
                                                <th>Price</th>
                                                <th>Total</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="item" items="${invoice.invoiceDetails}">
                                                <tr>
                                                    <td>${item.product.productID}</td>
                                                    <td>${item.product.name}</td>
                                                    <td>${item.quantity}</td>
                                                    <td>${item.price}</td>
                                                    <td>${item.quantity * item.price}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <c:if test="${sessionScope.LOGIN_USER.roleID ne 'AD'}">
                                        <c:if test="${invoice.status != 'Delivered' && invoice.status != 'Canceled'}">
                                            <form action="MainController" method="POST" onsubmit="return confirm('Bạn có chắc muốn hủy đơn hàng này không?')">
                                                <input type="hidden" name="invoiceID" value="${invoice.invoiceID}">
                                                <button type="submit" name="action" value="CancelInvoice" class="btn btn-outline-danger btn-sm mt-2">
                                                    <i class="bi bi-trash3-fill"></i> Cancel Order
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${invoice.status eq 'Delivered' && !invoice.returned}">
                                            <form action="MainController" method="POST" class="mt-2">
                                                <input type="hidden" name="action" value="ReturnRequest">
                                                <input type="hidden" name="invoiceID" value="${invoice.invoiceID}">
                                                <div class="mb-2">
                                                    <textarea name="reason" class="form-control" name="reason" placeholder="Reason ..." required></textarea>
                                                </div>
                                                <button type="submit" class="btn btn-outline-warning btn-sm">Request return</button>
                                            </form>
                                        </c:if>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
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
