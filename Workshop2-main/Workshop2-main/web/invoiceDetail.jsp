<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/pageStyle.css">
        <title>Invoice Detail</title>
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
                <a href="MainController?action=SearchStock">Products List</a>
                <a href="MainController?action=SearchTransaction">Categories List</a>
                <a href="MainController?action=ViewCart">Carts List</a>
                <a class="active" href="MainController?action=ViewInvoiceDetail">Invoices List</a>
                <a href="MainController?action=ViewAlerts">Deliveries List</a>
                <a href="MainController?action=ViewAlerts">Customer Care</a>
                <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                    <a href="MainController?action=SearchUser">User List</a>
                </c:if>
            </div>

            <div class="main-content">
                <div class="header">
                    <h1>Welcome, <c:out value="${sessionScope.LOGIN_USER.fullName}"/></h1>
                    <a href="${pageContext.request.contextPath}/LogoutController" class="logout-link">Logout</a>
                </div>

                <hr>

                <h1>Invoice Detail</h1>
                <c:if test="${not empty requestScope.MSG}">
                    <div class="msg">${requestScope.MSG}</div>
                </c:if>
                <c:if test="${not empty requestScope.INVOICE}">
                    <p><strong>Invoice ID:</strong> #${requestScope.INVOICE.invoiceID}</p>
                    <p><strong>User ID:</strong> ${requestScope.INVOICE.userID}</p>
                    <p><strong>Date:</strong> <fmt:formatDate value="${requestScope.INVOICE.createdDate}" pattern="dd/MM/yyyy"/></p>
                    <p><strong>Status:</strong> ${requestScope.INVOICE.status}</p>
                    <p><strong>Total:</strong> <fmt:formatNumber value="${requestScope.INVOICE.totalAmount}" type="currency" currencySymbol="" pattern="#,##0"/>₫</p>
                </c:if>

                <h3>Items</h3>
                <c:if test="${empty requestScope.INVOICE_DETAILS}">
                    <p>No items.</p>
                </c:if>
                <c:if test="${not empty requestScope.INVOICE_DETAILS}">
                    <table>
                        <thead>
                            <tr>
                                <th>Product ID</th>
                                <th>Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="entry" items="${requestScope.INVOICE_DETAILS}">
                                <tr>
                                    <td>${entry.key.productID}</td>
                                    <td>${entry.key.name}</td>
                                    <td><fmt:formatNumber value="${entry.value.price}" type="currency" currencySymbol="" pattern="#,##0"/>₫</td>
                                    <td>${entry.value.quantity}</td>
                                    <td><fmt:formatNumber value="${entry.value.price * entry.value.quantity}" type="currency" currencySymbol="" pattern="#,##0"/>₫</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>
            </div>
        </div>
    </body>
</html>