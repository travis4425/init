<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lỗi Hệ Thống</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fdf2f2;
            color: #b00020;
            padding: 40px;
        }
        .error-container {
            border: 1px solid #ffccd5;
            background-color: #fff0f1;
            padding: 20px;
            max-width: 600px;
            margin: auto;
            border-radius: 8px;
        }
        h1 {
            color: #b00020;
        }
        .back-link {
            margin-top: 20px;
            display: inline-block;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>Đã xảy ra lỗi</h1>
        <p><strong>Thông báo:</strong></p>
        <p>
            <c:choose>
                <c:when test="${not empty error}">
                    ${error}
                </c:when>
                <c:otherwise>
                    Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.
                </c:otherwise>
            </c:choose>
        </p>

        <a class="back-link" href="MainController?action=SearchUser">← Quay lại trang danh sách</a>
    </div>
</body>
</html>