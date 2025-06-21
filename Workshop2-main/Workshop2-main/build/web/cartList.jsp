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
    <title>Cart List Page</title>
    <style>
        /* Additional styles for cart list */
        .status-active { color: #198754; }
        .status-inactive { color: #dc3545; }
        .status-pending { color: #ffc107; }
        .status-checkout { color: #0d6efd; }
        
        .cart-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .cart-table th,
        .cart-table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        .cart-table th {
            background-color: #343a40;
            color: white;
            font-weight: 600;
        }
        
        .cart-table tr:hover {
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
        
        .status-badge.active {
            background-color: #d1edff;
            color: #0c5460;
        }
        
        .status-badge.inactive {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .status-badge.pending {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-badge.checkout {
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
        
        .btn-edit {
            background-color: #28a745;
            color: white;
        }
        
        .btn-delete {
            background-color: #dc3545;
            color: white;
        }
        
        .btn-checkout {
            background-color: #17a2b8;
            color: white;
        }
        
        .btn-clear {
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
        .stat-card.active { border-left-color: #28a745; }
        .stat-card.inactive { border-left-color: #dc3545; }
        .stat-card.pending { border-left-color: #ffc107; }
        
        .stat-number {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .no-carts {
            text-align: center;
            padding: 40px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .no-carts i {
            font-size: 48px;
            color: #ccc;
            margin-bottom: 20px;
        }
        
        .cart-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .cart-id {
            font-size: 12px;
            color: #666;
        }
        
        .price-display {
            color: #28a745;
            font-weight: bold;
        }
        
        .quantity-badge {
            background-color: #e9ecef;
            color: #495057;
            padding: 2px 6px;
            border-radius: 10px;
            font-size: 11px;
            font-weight: bold;
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
            <a class="active" href="MainController?action=ViewCartList">Carts List</a>
            <a href="MainController?action=ViewInvoiceDetail">Invoices List</a>
            <a href="MainController?action=ViewAlerts">Deliveries List</a>
            <a href="MainController?action=ViewAlerts">Customer Care</a>
            <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                <a href="MainController?action=SearchUser">User List</a>
            </c:if>
        </div>

        <div class="main-content">
            <h1><i class="fas fa-shopping-cart"></i> Danh sách giỏ hàng</h1>
            <p style="color: #666; margin-bottom: 30px;">Quản lý và theo dõi các giỏ hàng của khách hàng</p>

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
                    <form action="CartController" method="GET">
                        <input type="hidden" name="action" value="search">
                        <input type="text" name="userID" placeholder="Nhập User ID" value="${param.userID}">
                        <select name="status">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Active" ${param.status eq 'Active' ? 'selected' : ''}>Đang hoạt động</option>
                            <option value="Inactive" ${param.status eq 'Inactive' ? 'selected' : ''}>Không hoạt động</option>
                            <option value="Pending" ${param.status eq 'Pending' ? 'selected' : ''}>Chờ xử lý</option>
                            <option value="Checkout" ${param.status eq 'Checkout' ? 'selected' : ''}>Đã thanh toán</option>
                        </select>
                        <input type="date" name="fromDate" value="${param.fromDate}" placeholder="Từ ngày">
                        <input type="date" name="toDate" value="${param.toDate}" placeholder="Đến ngày">
                        <button type="submit" class="search-btn">
                            <i class="fas fa-search"></i> Tìm kiếm
                        </button>
                        <a href="MainController?action=ViewCartList" class="search-btn" style="background-color: #6c757d;">
                            <i class="fas fa-refresh"></i> Làm mới
                        </a>
                    </form>
                </div>
            </c:if>

            <!-- Cart Statistics -->
            <div class="stats-container">
                <div class="stat-card total">
                    <div class="stat-number">${not empty requestScope.CART_LIST ? requestScope.CART_LIST.size() : 0}</div>
                    <div>Tổng giỏ hàng</div>
                </div>
                <div class="stat-card active">
                    <div class="stat-number">
                        <c:set var="activeCount" value="0"/>
                        <c:forEach var="cart" items="${requestScope.CART_LIST}">
                            <c:if test="${cart.status eq 'Active'}">
                                <c:set var="activeCount" value="${activeCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${activeCount}
                    </div>
                    <div>Đang hoạt động</div>
                </div>
                <div class="stat-card pending">
                    <div class="stat-number">
                        <c:set var="pendingCount" value="0"/>
                        <c:forEach var="cart" items="${requestScope.CART_LIST}">
                            <c:if test="${cart.status eq 'Pending'}">
                                <c:set var="pendingCount" value="${pendingCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${pendingCount}
                    </div>
                    <div>Chờ xử lý</div>
                </div>
                <div class="stat-card inactive">
                    <div class="stat-number">
                        <c:set var="inactiveCount" value="0"/>
                        <c:forEach var="cart" items="${requestScope.CART_LIST}">
                            <c:if test="${cart.status eq 'Inactive' || cart.status eq 'Checkout'}">
                                <c:set var="inactiveCount" value="${inactiveCount + 1}"/>
                            </c:if>
                        </c:forEach>
                        ${inactiveCount}
                    </div>
                    <div>Không hoạt động</div>
                </div>
            </div>

            <!-- Cart List -->
            <c:choose>
                <c:when test="${empty requestScope.CART_LIST}">
                    <div class="no-carts">
                        <i class="fas fa-shopping-cart"></i>
                        <h3>Không có giỏ hàng nào</h3>
                        <p>Chưa có khách hàng nào tạo giỏ hàng hoặc không tìm thấy kết quả phù hợp.</p>
                        <a href="MainController?action=SearchStock" class="action-btn btn-view">
                            <i class="fas fa-plus"></i> Thêm sản phẩm
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Mã giỏ hàng</th>
                                <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                    <th>User ID</th>
                                </c:if>
                                <th>Ngày tạo</th>
                                <th>Số sản phẩm</th>
                                <th>Tổng số lượng</th>
                                <th>Tổng tiền</th>
                                <th>Trạng thái</th>
                                <th>Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="cart" items="${requestScope.CART_LIST}">
                                <tr>
                                    <td>
                                        <div class="cart-info">
                                            <i class="fas fa-shopping-cart"></i>
                                            <div>
                                                <strong>#${cart.cartID}</strong>
                                                <div class="cart-id">Cart-${cart.cartID}</div>
                                            </div>
                                        </div>
                                    </td>
                                    <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                        <td>
                                            <i class="fas fa-user"></i> ${cart.userID}
                                        </td>
                                    </c:if>
                                    <td>
                                        <fmt:formatDate value="${cart.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td>
                                        <span class="quantity-badge">${cart.itemCount} sản phẩm</span>
                                    </td>
                                    <td>
                                        <strong>${cart.totalQuantity}</strong>
                                    </td>
                                    <td>
                                        <span class="price-display">
                                            <fmt:formatNumber value="${cart.totalAmount}" type="currency" 
                                                            currencySymbol="" pattern="#,##0"/>₫
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${cart.status eq 'Active'}">
                                                <span class="status-badge active">
                                                    <i class="fas fa-check-circle"></i> Đang hoạt động
                                                </span>
                                            </c:when>
                                            <c:when test="${cart.status eq 'Inactive'}">
                                                <span class="status-badge inactive">
                                                    <i class="fas fa-times-circle"></i> Không hoạt động
                                                </span>
                                            </c:when>
                                            <c:when test="${cart.status eq 'Pending'}">
                                                <span class="status-badge pending">
                                                    <i class="fas fa-clock"></i> Chờ xử lý
                                                </span>
                                            </c:when>
                                            <c:when test="${cart.status eq 'Checkout'}">
                                                <span class="status-badge checkout">
                                                    <i class="fas fa-credit-card"></i> Đã thanh toán
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge">${cart.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="CartController?action=view&cartID=${cart.cartID}" 
                                           class="action-btn btn-view" title="Xem chi tiết">
                                            <i class="fas fa-eye"></i>
                                        </a>
                                        
                                        <c:if test="${cart.status eq 'Active' && (sessionScope.LOGIN_USER.userID eq cart.userID || sessionScope.LOGIN_USER.roleID eq 'AD')}">
                                            <a href="CartController?action=edit&cartID=${cart.cartID}" 
                                               class="action-btn btn-edit" title="Chỉnh sửa">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                        </c:if>
                                        
                                        <c:if test="${cart.status eq 'Active' && sessionScope.LOGIN_USER.userID eq cart.userID}">
                                            <a href="InvoiceController?action=Checkout&cartID=${cart.cartID}" 
                                               class="action-btn btn-checkout" title="Thanh toán">
                                                <i class="fas fa-credit-card"></i>
                                            </a>
                                        </c:if>
                                        
                                        <c:if test="${sessionScope.LOGIN_USER.userID eq cart.userID || sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                            <button type="button" class="action-btn btn-clear" 
                                                    onclick="clearCart(${cart.cartID})" title="Xóa giỏ hàng">
                                                <i class="fas fa-trash-alt"></i>
                                            </button>
                                        </c:if>
                                        
                                        <c:if test="${sessionScope.LOGIN_USER.roleID eq 'AD'}">
                                            <button type="button" class="action-btn btn-delete" 
                                                    onclick="deleteCart(${cart.cartID})" title="Xóa vĩnh viễn">
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
        function clearCart(cartID) {
            if (confirm('Bạn có chắc chắn muốn xóa tất cả sản phẩm trong giỏ hàng #' + cartID + '?')) {
                window.location.href = 'CartController?action=clear&cartID=' + cartID;
            }
        }

        function deleteCart(cartID) {
            if (confirm('Bạn có chắc chắn muốn xóa vĩnh viễn giỏ hàng #' + cartID + '? Hành động này không thể hoàn tác!')) {
                window.location.href = 'CartController?action=delete&cartID=' + cartID;
            }
        }

        // Auto refresh every 30 seconds for active carts
        setTimeout(function() {
            if (window.location.search.indexOf('autoRefresh=false') === -1) {
                window.location.reload();
            }
        }, 30000);
    </script>
</body>
</html>