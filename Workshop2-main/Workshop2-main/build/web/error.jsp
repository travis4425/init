<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Error</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body { background-color: #f8f9fa; display: flex; align-items: center; justify-content: center; height: 100vh; }
            .error-container { text-align: center; }
        </style>
    </head>
    <body>
        <div class="error-container">
            <h2>An error occurred</h2>
            <p class="text-danger"><c:out value="${ERROR}"/></p>
            <a href="javascript:history.back()" class="btn btn-primary mt-3">Go Back</a>
        </div>
    </body>
</html>