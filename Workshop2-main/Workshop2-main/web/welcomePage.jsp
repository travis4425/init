<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Welcome Page</title>
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
            .welcome-section {
                background: linear-gradient(135deg, #6e8efb, #a777e3);
                color: white;
                border-radius: 15px;
                padding: 60px 30px;
                text-align: center;
                box-shadow: 0 4px 15px rgba(0,0,0,0.2);
            }
            .welcome-section h1 {
                font-size: 3rem;
                font-weight: 700;
                margin-bottom: 20px;
            }
            .welcome-section p {
                font-size: 1.25rem;
                opacity: 0.9;
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
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2>Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/></h2>
                        <a href="${pageContext.request.contextPath}/LogoutController" class="btn btn-danger">Logout</a>
                    </div>

                    <!-- Welcome Section -->
                    <div class="welcome-section">
                        <h1>Hello, <c:out value="${sessionScope.LOGIN_USER.fullName}"/>!</h1>
                        <p>Welcome to the <c:out value="${sessionScope.LOGIN_USER.roleID}"/> Dashboard. Manage your products, categories, and more with ease.</p>
                        <div class="mt-4">
                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                <a href="MainController?action=ViewProducts" class="btn btn-light btn-lg mx-2">View Products</a>
                                <a href="MainController?action=ViewCategories" class="btn btn-outline-light btn-lg mx-2">View Categories</a>
                            </c:if>

                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'SE'}">
                                <a href="MainController?action=ViewProducts" class="btn btn-light btn-lg mx-2">My Products</a>
                            </c:if>

                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'BU'}">
                                <a href="MainController?action=ViewProducts" class="btn btn-light btn-lg mx-2">View Products</a>
                                <a href="MainController?action=ViewCategories" class="btn btn-outline-light btn-lg mx-2">View Categories</a>
                            </c:if>

                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'MK'}">
                                <a href="MainController?action=ViewProducts" class="btn btn-light btn-lg mx-2">View Products</a>
                                <a href="PromotionController" class="btn btn-outline-light btn-lg mx-2">View Promotions</a>
                            </c:if>

                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'CS'}">
                                <a href="MainController?action=CustomerCare" class="btn btn-light btn-lg mx-2">View Requests</a>
                            </c:if>

                            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'DL'}">
                                <a href="MainController?action=ViewDelivery" class="btn btn-light btn-lg mx-2">View Delivery</a>
                            </c:if>    
                        </div>
                    </div>

                    <!-- Message -->
                    <c:if test="${not empty MSG}">
                        <div id="msg" class="alert alert-fixed alert-${MSG.contains('Failed') ? 'danger' : 'success'} alert-dismissible fade show" role="alert">
                            ${MSG}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
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