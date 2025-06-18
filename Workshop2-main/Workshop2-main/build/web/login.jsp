<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

    <!-- Icon + Bootstrap -->
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">

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
        <h2>Login</h2>

        <% if (request.getAttribute("MSG") != null) { %>
            <p class="error-msg"><%= request.getAttribute("MSG") %></p>
        <% } %>

        <form action="MainController" method="post">
            <div class="input-box">
                <input type="text" name="userID" placeholder="Username" required>
                <i class='bx bxs-user'></i>
            </div>

            <div class="input-box">
                <input type="password" name="password" placeholder="Password" required>
                <i class='bx bxs-lock-alt'></i>
            </div>

            <div class="remember-forgot">
                <label><input type="checkbox"> Remember me</label>
                <a href="#">Forgot password?</a>
            </div>

            <input type="submit" name="action" value="Login">

            <div class="register-link">
                <p>Don't have an account? <a href="register.jsp">Register</a></p>
            </div>
        </form>
    </div>
</div>

</body>
</html>
