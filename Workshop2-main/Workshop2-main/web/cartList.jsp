<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Shopping Cart</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .cart-table th,
        .cart-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .cart-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .cart-total {
            text-align: right;
            font-size: 18px;
            font-weight: bold;
            color: #28a745;
            margin: 20px 0;
        }
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 5px;
            font-size: 14px;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-success {
            background-color: #28a745;
            color: white;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn:hover {
            opacity: 0.8;
        }
        .quantity-input {
            width: 60px;
            padding: 5px;
            text-align: center;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .empty-cart {
            text-align: center;
            padding: 50px;
            color: #666;
        }
        .actions {
            text-align: center;
            margin-top: 20px;
        }
        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
            text-align: center;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #eee;
        }
        .header .user-info {
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Shopping Cart</h1>
            <div class="user-info">
                Welcome, ${sessionScope.LOGIN_USER.fullName}!
                <a href="LogoutController" class="btn btn-secondary">Logout</a>
            </div>
        </div>

        <c:if test="${not empty requestScope.MSG}">
            <div class="message success">${requestScope.MSG}</div>
        </c:if>

        <c:choose>
            <c:when test="${empty requestScope.cartList}">
                <div class="empty-cart">
                    <h2>Your cart is empty</h2>
                    <p>Add some products to your cart to see them here.</p>
                    <a href="SearchUserController" class="btn btn-primary">Continue Shopping</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${requestScope.cartList}">
                            <tr>
                                <td>${item.productID}</td>
                                <td>${item.productName}</td>
                                <td>$<fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2"/></td>
                                <td>
                                    <form method="post" action="CartController" style="display: inline;">
                                        <input type="hidden" name="action" value="UpdateCart">
                                        <input type="hidden" name="productID" value="${item.productID}">
                                        <input type="number" name="quantity" value="${item.quantity}" 
                                               class="quantity-input" min="1" max="99">
                                        <button type="submit" class="btn btn-primary">Update</button>
                                    </form>
                                </td>
                                <td>$<fmt:formatNumber value="${item.total}" type="number" minFractionDigits="2"/></td>
                                <td>
                                    <a href="CartController?action=RemoveFromCart&productID=${item.productID}" 
                                       class="btn btn-danger"
                                       onclick="return confirm('Are you sure you want to remove this item?')">Remove</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="cart-total">
                    Total Amount: $<fmt:formatNumber value="${requestScope.totalAmount}" type="number" minFractionDigits="2"/>
                </div>

                <div class="actions">
                    <a href="SearchUserController" class="btn btn-secondary">Continue Shopping</a>
                    <a href="CartController?action=ClearCart" class="btn btn-danger"
                       onclick="return confirm('Are you sure you want to clear your cart?')">Clear Cart</a>
                    <a href="InvoiceController?action=Checkout" class="btn btn-success">Checkout</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>