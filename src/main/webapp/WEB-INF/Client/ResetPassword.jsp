<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu - Việt Nam Travel</title>
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
        <p>Đặt lại mật khẩu mới</p>
    </div>

    <div class="auth-body">
        <!-- Hiển thị thông báo thành công -->
        <c:if test="${not empty success}">
            <div class="success-message">${success}</div>
        </c:if>

        <!-- Hiển thị thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <c:if test="${empty success}">
            <form action="<%= request.getContextPath() %>/ResetPasswordServlet" method="post">
                <!-- CSRF Token -->
                <input type="hidden" name="_csrf_token" value="${_csrf_token}">
                
                <!-- Hidden token field -->
                <input type="hidden" name="token" value="${token}">
            
                <div class="form-group">
                    <label for="password">Mật khẩu mới</label>
                    <input type="password" id="password" name="password" required 
                           placeholder="Tối thiểu 8 ký tự">
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu mới</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required
                           placeholder="Nhập lại mật khẩu mới">
                </div>

                <button type="submit" class="btn-primary">
                    <i class="fas fa-key"></i> Đặt lại mật khẩu
                </button>
            </form>
        </c:if>
    </div>

    <div class="auth-footer">
        <c:if test="${empty success}">
            <a href="${pageContext.request.contextPath}/LoginServlet">Quay lại đăng nhập</a>
        </c:if>
        <c:if test="${not empty success}">
            <a href="${pageContext.request.contextPath}/LoginServlet">Đăng nhập ngay</a>
        </c:if>
    </div>
</div>

</body>
</html>