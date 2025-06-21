<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/pageStyle.css"> 
        <title>Product List Page</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${empty sessionScope.LOGIN_USER}">
                <c:redirect url="login.jsp"/>
            </c:when>
        </c:choose>
        
        <div class="container">
            <div class="sidebar">
                <h2>Menu</h2>
                <a class="active" href="MainController?action=SearchStock">Products List</a>
                <a href="MainController?action=SearchTransaction">Categories List</a>
                <a href="MainController?action=ViewCart">Carts List</a>
                <a href="MainController?action=ViewInvoiceDetail">Invoices List</a>
                <a href="MainController?action=ViewAlerts">Deliveries List</a>
                <a href="MainController?action=ViewAlerts">Customer Care</a>
                <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                    <a href="MainController?action=SearchUser">User List</a>
                </c:if>
            </div>
            
            <div class="main-content">
                
            </div>
            
        </div>
    </body>
</html>