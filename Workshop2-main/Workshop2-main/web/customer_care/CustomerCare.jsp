<%@page import="dto.CustomerCare"%>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

    <head>
        <meta charset="UTF-8">
        <title>Customer Care Page</title>
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
                      sessionScope.LOGIN_USER.roleID ne 'CS' && 
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
                          <a href="PromotionController">Promotion</a>
                    </c:if>
                    <!-- customer care -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD' || 
                                  sessionScope.LOGIN_USER.roleID eq 'BU' || 
                                  sessionScope.LOGIN_USER.roleID eq 'CS'}">
                          <a class="active" href="MainController?action=CustomerCare">Customer Care</a>
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
                        <div class="card-header">Search Tickets</div>
                        <div class="card-body">
                            <form class="row g-3" action="MainController" method="POST">
                                <div class="col-md-6">
                                    <input type="text" class="form-control" name="search" placeholder="Search by subject" value="${param.search != null ? param.search : ''}">
                                </div>
                                <div class="col-md-6 d-flex gap-2">
                                    <button type="submit" name="action" value="SearchCustomerCare" class="btn btn-primary w-100">Search</button>
                                    <c:if test="${param.search != null && param.search != ''}">
                                        <a href="MainController?action=CustomerCare" class="btn btn-secondary w-100">Clear</a>
                                    </c:if>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- Create Form Toggle (BU only) -->
                    <c:if test="${sessionScope.LOGIN_USER.roleID == 'BU'}">
                        <div class="mb-4">
                            <button class="btn btn-success" type="button" data-bs-toggle="collapse" data-bs-target="#createTicketForm">
                                Create New Ticket
                            </button>
                        </div>

                        <!-- Create Ticket Form -->
                        <div class="collapse mb-4" id="createTicketForm">
                            <div class="card">
                                <div class="card-header">Create New Ticket</div>
                                <div class="card-body">
                                    <form action="MainController" method="POST" class="row g-3">
                                        <input type="hidden" name="action" value="CreateCustomerCare"/>
                                        <div class="col-md-6">
                                            <label class="form-label">Subject</label>
                                            <input type="text" class="form-control" name="subject" required>
                                        </div>
                                        <div class="col-md-12">
                                            <label class="form-label">Content</label>
                                            <textarea name="content" class="form-control" rows="4" required></textarea>
                                        </div>
                                        <div class="col-12 d-flex justify-content-end">
                                            <button type="submit" class="btn btn-success">Submit</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <h2>Ticket List</h2>

                    <c:if test="${empty listCare}">
                        <div class="alert alert-warning">No matching tickets found!</div>
                    </c:if>

                    <c:if test="${not empty listCare}">
                        <div class="table-responsive">
                            <table class="table table-bordered table-hover align-middle">
                                <thead class="table-primary">
                                    <tr>
                                        <th>No</th>
                                        <th>User ID</th>
                                        <th>Subject</th>
                                        <th>Content</th>
                                        <th>Status</th>
                                        <th>Reply</th>
                                            <c:if test="${sessionScope.LOGIN_USER.roleID == 'AD' || sessionScope.LOGIN_USER.roleID == 'CS'}">
                                            <th>Actions</th>
                                            </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${listCare}" varStatus="st">
                                        <tr>
                                    <form action="MainController" method="POST">
                                        <input type="hidden" name="ticketID" value="${item.ticketID}"/>
                                        <input type="hidden" name="search" value="${param.search}"/>
                                        <td>${st.count}</td>
                                        <td>${item.userID}</td>
                                        <td>${item.subject}</td>
                                        <td>${item.content}</td>
                                        <td>${item.status}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty item.reply}">
                                                    <c:if test="${sessionScope.LOGIN_USER.roleID == 'CS'}">
                                                        <textarea name="reply" class="form-control" rows="2" required></textarea>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    ${item.reply}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <c:if test="${sessionScope.LOGIN_USER.roleID == 'AD' || sessionScope.LOGIN_USER.roleID == 'CS'}">
                                            <td>
                                                <div class="d-flex flex-column gap-2">

                                                    <c:if test="${empty item.reply && sessionScope.LOGIN_USER.roleID == 'CS'}">
                                                        <button type="submit" name="action" value="ReplyCustomerCare" class="btn btn-primary btn-sm w-100">Reply</button>
                                                    </c:if>
                                                    <button type="submit" name="action" value="DeleteCustomerCare" class="btn btn-danger btn-sm w-100" onclick="return confirm('Are you sure to delete this ticket?')">Delete</button>

                                                </div>
                                            </td>
                                        </c:if>
                                    </form>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:if>

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