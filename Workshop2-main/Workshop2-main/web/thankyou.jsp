<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Order Confirmation</title>
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
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .thank-you-header {
            text-align: center;
            color: #28a745;
            margin-bottom: 30px;
        }
        .thank-you-header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
        }
        .thank-you-header .checkmark {
            font-size: 4em;
            color: #28a745;
            margin-bottom: 20px;
        }
        .order-summary {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin: 20px 0;
        }
        .order-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        .info-item {
            padding: 10px;
            background-color: white;
            border-radius: 4px;
            border-left: 4px solid #007bff;
        }
        .info-label {
            font-weight: bold;
            color: #666;
            font-size: 0.9em;
            margin-bottom: 5px;
        }
        .info-value {
            font-size: 1.1em;
            color: #333;
        }
        .items-table {
            width: 100%;
            border-collapse: collapse;
            margin: 20px 0;
        }
        .items-table th,
        .items-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .items-table th {
            background-color: #f8f9fa;
            font-weight: bold;
        }
        .total-amount {
            text-align: right;
            font-size: 1.5em;
            font-weight: bold;
            color: #28a745;
            margin: 20px 0;
            padding: 15px;
            background-color: #d4edda;
            border-radius: 8px;
        }
        .actions {
            text-align: center;
            margin-top: 30px;
        }
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 5px 10px;
            font-size: 16px;
            transition: all 0.3s ease;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-success {
            background-color: #28a745;
            color: white;
        }
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        .message {
            padding: 15px;
            margin: 15px 0;
            border-radius: 4px;
            text-align: center;
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="thank-you-header">
            <div class="checkmark">✓</div>
            <h1>Thank You!</h1>
            <p>Your order has been placed successfully</p>
        </div>

        <c:if test="${not empty requestScope.MSG}">
            <div class="message">${requestScope.MSG}</div>
        </c:if>

        <c:if test="${not empty requestScope.invoice}">
            <div class="order-summary">
                <h2>Order Summary</h2>
                
                <div class="order-info">
                    <div class="info-item">
                        <div class="info-label">Invoice ID</div>
                        <div class="info-value">${requestScope.invoice.invoiceID}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Order Date</div>
                        <div class="info-value">
                            <fmt:formatDate value="${requestScope.invoice.invoiceDate}" pattern="MMM dd, yyyy"/>
                        </div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Customer ID</div>
                        <div class="info-value">${requestScope.invoice.userID}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">Status</div>
                        <div class="info-value">${requestScope.invoice.status}</div>
                    </div>
                </div>

                <h3>Ordered Items</h3>
                <table class="items-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${requestScope.invoiceDetails}">
                            <tr>
                                <td>
                                    <strong>${item.productName}</strong><br>
                                    <small>ID: ${item.productID}</small>
                                </td>
                                <td>$<fmt:formatNumber value="${item.price}" type="number" minFractionDigits="2"/></td>
                                <td>${item.quantity}</td>
                                <td>$<fmt:formatNumber value="${item.total}" type="number" minFractionDigits="2"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="total-amount">
                    Total Amount: $<fmt:formatNumber value="${requestScope.invoice.totalAmount}" type="number" minFractionDigits="2"/>
                </div>
            </div>
        </c:if>

        <div class="actions">
            <a href="InvoiceController?action=ViewInvoices" class="btn btn-primary">View My Orders</a>
            <a href="SearchUserController" class="btn btn-success">Continue Shopping</a>
        </div>

        <div style="text-align: center; margin-top: 30px; color: #666; font-size: 0.9em;">
            <p>A confirmation email has been sent to your registered email address.</p>
            <p>Thank you for shopping with us!</p>
        </div>
    </div>
</body>
</html>