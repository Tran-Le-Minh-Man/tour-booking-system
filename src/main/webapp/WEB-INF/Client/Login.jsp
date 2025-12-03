<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Việt Nam Travel</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/Auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
    :root {
            --bg-image: url(../Images/Login.png);
        }
    </style>
</head>
<body>

<div class="auth-container">
    <div class="auth-header">
        <h1>Việt Nam Travel</h1>
        <p>Chào mừng quay lại! Đăng nhập để tiếp tục hành trình</p>
    </div>

    <div class="auth-body">
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="<%= request.getContextPath() %>/LoginServlet" method="post">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required placeholder="you@example.com" value="${cookie.email.value}">
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                <label style="font-weight:normal; font-size:0.9rem;">
                    <input type="checkbox" name="remember" ${cookie.email != null ? 'checked' : ''}>
                    Ghi nhớ đăng nhập
                </label>
                <a href="#" style="font-size:0.9rem; color:#0C67B3;">Quên mật khẩu?</a>
            </div>

            <button type="submit" class="btn-primary">
                <i class="fas fa-sign-in-alt"></i> Đăng nhập
            </button>
        </form>
    </div>

    <div class="auth-footer">
        Chưa có tài khoản? <a href="${pageContext.request.contextPath}/Register">Đăng ký miễn phí</a>
    </div>
</div>

</body>
</html>