<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="dto.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/pageStyle.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <title>Invoice List Page</title>
    <style>
        /* Additional styles for invoice list */
        .status-pending { color: #ffc107; }
        .status-completed { color: #198754; }
        .status-cancelled { color: #dc3545; }
        .status-shipped { color: #0d6efd; }
        
        .invoice-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .invoice-table th,
        .invoice-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        .invoice-table th {
            background-color: #343a40;
            color: white;
            font-weight: 600;
        }
        
        .invoice-table tr:hover {
            background-color: #f8f9fa;
        }
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: bold;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }
        
        .status-badge.pending {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-badge.completed {
            background-color: #d1edff;
            color: #0c5460;
        }
        
        .status-badge.cancelled {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .status-badge.shipped {
            background-color: #cce7ff;
            color: #004085;
        }
        
        .action-btn {
            padding: 6px 12px;
            margin: 2px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            transition: all 0.3s;
        }
        
        .btn-view {
            background-color: #007bff;
            color: white;
        }
        
        .btn-cancel {
            background-color: #dc3545;
            color: white;
        }
        
        .btn-update {
            background-color: #28a745;
            color: white;
        }
        
        .btn-delete {
            background-color: #6c757d;
            color: white;
        }
        
        .action-btn:hover {
            opacity: 0.8;
            transform: translateY(-1px);
        }
        
        .search-form {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .search-form input,
        .search-form select {
            padding: 8px 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 5px;
        }
        
        .search-btn {
            background-color: #007bff;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin: 5px;
        }
        
        .stats-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            border-left: 4px solid;
        }
        
        .stat-card.total { border-left-color: #007bff; }
        .stat-card.pending { border-left-color: #ffc107; }
        .stat-card.completed { border-left-color: #28a745; }
        .stat-card.cancelled { border-left-color: #dc3545; }
        
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .no-invoices {
            text-align: center;
            padding: 40px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .no-invoices i {
            font-size: 48px;
            color: #ccc;
            margin-bottom: 20px;
        }
    </style>
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
            <h1><i class="fas fa-receipt"></i> Danh sách hóa đơn</h1>
            <p style="color: #666; margin-bottom: 30px;">Quản lý và theo dõi các hóa đơn của bạn</p>

            <!-- Messages -->
            <c:if test="${not empty requestScope.MSG}">
                <div style="background: #d1ecf1; color: #0c5460; padding: 12px; border-radius: 4px; margin-bottom: 20px; border-left: 4px solid #17a2b8;">
                    <i class="fas fa-info-circle"></i> ${requestScope.MSG}
                </div>
            </c:if>

            <!-- Search & Filter (Admin only) -->
            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                <div class="search-form">
                    <h3><i class="fas fa-search"></i> Tìm kiếm & Lọc</h3>
                    <form action="InvoiceController" method="GET">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="userID" placeholder="Nhập User ID">
                        <select name="status">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Pending">Chờ xử lý</option>
                            <option value="Completed">Hoàn thành</option>
                            <option value="Shipped">Đã gửi</option>
                            <option value="Cancelled">Đã hủy</option>
                        </select>
                        <input type="date" name="fromDate" placeholder="Từ ngày">
                        <input type="date" name="toDate" placeholder="Đến ngày">
                        <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i> Tìm kiếm
                        </button>
                    </form>
                </div>
            </c:if>

            <!-- Invoice Statistics -->
            <div class="stats-container">
                <div class="stat-card total">
                    <div class="stat-number">${not empty requestScope.INVOICES ? requestScope.INVOICES.size() : 0}</div>
                    <div>Tổng hóa đơn</div>
                </div>
                <div class="stat-card pending">
                    <div class="stat-number">
                        <c:set var="pendingCount" value="0"/>
                        <c:forEach var="invoice" items="${requestScope.INVOICES}">
                            <c:if test="${invoice.status eq 'Pending'}">
                                <c:set var="pendingCount" value="${pendingCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${pendingCount}
                    </div>
                    <div>Chờ xử lý</div>
                </div>
                <div class="stat-card completed">
                    <div class="stat-number">
                        <c:set var="completedCount" value="0"/>
                        <c:forEach var="invoice" items="${requestScope.INVOICES}">
                            <c:if test="${invoice.status eq 'Completed'}">
                                <c:set var="completedCount" value="${completedCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${completedCount}
                    </div>
                    <div>Hoàn thành</div>
                </div>
                <div class="stat-card cancelled">
                    <div class="stat-number">
                        <c:set var="cancelledCount" value="0"/>
                        <c:forEach var="invoice" items="${requestScope.INVOICES}">
                            <c:if test="${invoice.status eq 'Cancelled'}">
                                <c:set var="cancelledCount" value="${cancelledCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${cancelledCount}
                    </div>
                    <div>Đã hủy</div>
                </div>
            </div>

            <!-- Invoice List -->
            <c:choose>
                <c:when test="${empty requestScope.INVOICES}">
                    <div class="no-invoices">
                        <i class="fas fa-receipt"></i>
                        <h3>Không có hóa đơn nào</h3>
                        <p>Hãy mua sắm để tạo hóa đơn đầu tiên!</p>
                        <a href="ProductController" class="action-btn btn-view">
                            <i class="fas fa-shopping-cart"></i> Mua sắm ngay
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="invoice-table">
                        <thead>
                            <tr>
                                <th>Mã hóa đơn</th>
                                <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                    <th>User ID</th>
                                </c:if>
                                <th>Ngày tạo</th>
                                <th>Trạng thái</th>
                                <th>Tổng tiền</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="invoice" items="${requestScope.INVOICES}">
                                <tr>
                                    <td><strong>#${invoice.invoiceID}</strong></td>
                                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                        <td>
                                            <i class="fas fa-user"></i> ${invoice.userID}
                                        </td>
                                    </c:if>
                                    <td>
                                        <fmt:formatDate value="${invoice.createdDate}" pattern="dd/MM/yyyy"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${invoice.status eq 'Pending'}">
                                                <span class="status-badge pending">
                                                    <i class="fas fa-clock"></i> Chờ xử lý
                                                </span>
                                            </c:when>
                                            <c:when test="${invoice.status eq 'Completed'}">
                                                <span class="status-badge completed">
                                                    <i class="fas fa-check"></i> Hoàn thành
                                                </span>
                                            </c:when>
                                            <c:when test="${invoice.status eq 'Cancelled'}">
                                                <span class="status-badge cancelled">
                                                    <i class="fas fa-times"></i> Đã hủy
                                                </span>
                                            </c:when>
                                            <c:when test="${invoice.status eq 'Shipped'}">
                                                <span class="status-badge shipped">
                                                    <i class="fas fa-truck"></i> Đã gửi
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge">${invoice.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <strong style="color: #28a745;">
                                            <fmt:formatNumber value="${invoice.totalAmount}" type="currency" 
                                                            currencySymbol="" pattern="#,##0"/>₫
                                        </strong>
                                    </td>
                                    <td>
                                        <a href="InvoiceController?action=view&invoiceID=${invoice.invoiceID}" 
                                           class="action-btn btn-view" title="Xem chi tiết">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        
                                        <c:if test="${invoice.status eq 'Pending'}">
                                            <button type="button" class="action-btn btn-cancel" 
                                                    onclick="confirmCancel(${invoice.invoiceID})" title="Hủy đơn">
                                                <i class="fas fa-ban"></i>
                                            </button>
                                        </c:if>
                                        
                                        <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                            <c:if test="${invoice.status ne 'Completed' && invoice.status ne 'Cancelled'}">
                                                <button type="button" class="action-btn btn-update" 
                                                        onclick="updateStatus(${invoice.invoiceID}, 'Completed')" title="Hoàn thành">
                                                    <i class="fas fa-check"></i>
                                                </button>
                                                <button type="button" class="action-btn btn-update" 
                                                        onclick="updateStatus(${invoice.invoiceID}, 'Shipped')" title="Đã gửi">
                                                    <i class="fas fa-truck"></i>
                                                </button>
                                            </c:if>
                                            <button type="button" class="action-btn btn-delete" 
                                                    onclick="deleteInvoice(${invoice.invoiceID})" title="Xóa">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        function confirmCancel(invoiceID) {
            if (confirm('Bạn có chắc chắn muốn hủy hóa đơn #' + invoiceID + '?')) {
                window.location.href = 'InvoiceController?action=cancel&invoiceID=' + invoiceID;
            }
        }

        function updateStatus(invoiceID, status) {
            var statusText = status === 'Completed' ? 'hoàn thành' : 'đã gửi';
            if (confirm('Bạn có chắc chắn muốn cập nhật trạng thái hóa đơn #' + invoiceID + ' thành ' + statusText + '?')) {
                window.location.href = 'InvoiceController?action=updateStatus&invoiceID=' + invoiceID + '&status=' + status;
            }
        }

        function deleteInvoice(invoiceID) {
            if (confirm('Bạn có chắc chắn muốn xóa hóa đơn #' + invoiceID + '? Hành động này không thể hoàn tác!')) {
                window.location.href = 'InvoiceController?action=delete&invoiceID=' + invoiceID;
            }
        }
    </script>
</body>
</html>