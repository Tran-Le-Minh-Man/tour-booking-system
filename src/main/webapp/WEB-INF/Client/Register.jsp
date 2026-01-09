<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản - Việt Nam Travel</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/Auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --bg-image: url(../Images/Register.jpg);
        }
    </style>
</head>
<body>

<div class="auth-container">
    <div class="auth-header">
        <h1>Việt Nam Travel</h1>
        <p>Đăng ký để đặt tour ngay hôm nay!</p>
    </div>

    <div class="auth-body">
        <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="success-message">${success}</div>
        </c:if>

        <form action="<%= request.getContextPath() %>/RegisterServlet" method="post">
            <!-- CSRF Token -->
            <input type="hidden" name="_csrf_token" value="${_csrf_token}">
            
            <div class="form-group">
                <label for="fullName">Họ và tên</label>
                <input type="text" id="fullName" name="fullName" required 
                       placeholder="Nguyễn Văn A" 
                       value="<c:out value='${fullName}'/>">
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required 
                       placeholder="you@example.com"
                       value="<c:out value='${email}'/>">
            </div>

            <div class="form-group">
                <label for="phone">Số điện thoại</label>
                <input type="tel" id="phone" name="phone" 
                       placeholder="0901234567 (tùy chọn)"
                       value="<c:out value='${phone}'/>">
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required 
                       placeholder="Tối thiểu 8 ký tự">
            </div>

            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required
                       placeholder="Nhập lại mật khẩu">
            </div>

            <button type="submit" class="btn-primary">
                <i class="fas fa-user-plus"></i> Đăng ký ngay
            </button>
        </form>
    </div>

    <div class="auth-footer">
        Đã có tài khoản? <a href="${pageContext.request.contextPath}/LoginServlet">Đăng nhập ngay</a>
    </div>
</div>

</body>
</html>