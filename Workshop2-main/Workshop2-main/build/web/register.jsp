<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
              rel="stylesheet"/>
        <style>
            body {
                margin: 0;
                font-family: Arial, sans-serif;
                background-color: #f9f8ff;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .wrapper {
                display: flex;
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                border-radius: 20px;
                overflow: hidden;
            }

            .image-section {
                background-color: #f0f0ff;
                padding: 20px;
                display: flex;
                align-items: center;
                justify-content: center;
            }

            .image-section img {
                border-radius: 10px;
                max-width: 300px;
                height: auto;
            }

            .login-container {
                padding: 40px 30px;
                width: 350px;
            }

            h2 {
                text-align: center;
                color: #3f51b5;
            }

            form {
                display: flex;
                flex-direction: column;
            }

            .input-box {
                position: relative;
            }

            .input-box input {
                width: 100%;
                padding: 10px 35px 10px 10px;
                margin: 8px 0;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            .input-box i {
                position: absolute;
                right: 10px;
                top: 50%;
                transform: translateY(-50%);
                color: #3f51b5;
            }

            input[type="submit"] {
                padding: 10px;
                margin-top: 15px;
                background-color: #3f51b5;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-weight: bold;
            }

            input[type="submit"]:hover {
                background-color: #303f9f;
            }

            .remember-forgot {
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 0.9em;
                margin-top: 8px;
            }

            .register-link {
                text-align: center;
                margin-top: 15px;
                font-size: 0.9em;
            }

            .error-msg {
                color: red;
                text-align: center;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <div class="wrapper">
            <div class="image-section">
                <img src="https://media.vneconomy.vn/w800/images/upload/2023/08/04/tmdt-2023.png" alt="E-commerce Image">
            </div>
            <div class="login-container">
                <h2>Register</h2>
                <c:if test="${not empty MSG}">
                    <p class="error-msg">${MSG}</p>
                </c:if>
                <form action="MainController" method="post">
                    <input type="hidden" name="action" value="Register"/>
                    <div class="input-box">
                        <input type="text" name="userID" placeholder="Username" required/>
                        <i class='bx bxs-user'></i>
                    </div>
                    <div class="input-box">
                        <input type="text" name="fullName" placeholder="Full Name" required/>
                        <i class='bx bxs-id-card'></i>
                    </div>
                    <div class="input-box">
                        <input type="password" name="password" placeholder="Password" required/>
                        <i class='bx bxs-lock-alt'></i>
                    </div>
                    <div class="input-box">
                        <input type="text" name="phone" placeholder="Phone" required/>
                        <i class='bx bxs-phone'></i>
                    </div>
                    <!-- nếu muốn cho user chọn role, nhưng thường register mặc định buyer -->
                    <input type="submit" value="Register" class="btn btn-primary w-100 mt-3"/>
                    <div class="register-link text-center mt-2">
                        <p>Already have an account? <a href="login.jsp">Login</a></p>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
