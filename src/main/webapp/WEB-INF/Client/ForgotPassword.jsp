<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - Việt Nam Travel</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/Auth.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root {
            --bg-image: url(../Images/Login.png);
        }
        
        .info-box {
            background: #e7f1ff;
            border: 2px dashed #0d6efd;
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 1rem;
        }
        
        .info-box h4 {
            color: #0d6efd;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        
        .reset-link {
            background: white;
            border: 1px solid #0d6efd;
            padding: 10px;
            border-radius: 5px;
            word-break: break-all;
            font-family: monospace;
            font-size: 0.9rem;
            margin-top: 10px;
        }
        
        .reset-link a {
            color: #0d6efd;
            text-decoration: none;
        }
        
        .reset-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

<div class="auth-container">
    <div class="auth-header">
        <h1>Việt Nam Travel</h1>
        <p>Quên mật khẩu? Khôi phục tài khoản của bạn</p>
    </div>

    <div class="auth-body">
        <!-- Hiển thị thông báo thành công với reset link -->
        <c:if test="${not empty resetLink}">
            <div class="info-box">
                <h4><i class="fas fa-info-circle"></i> Link khôi phục mật khẩu</h4>
                <p>Chúng tôi đã gửi link khôi phục đến email của bạn!</p>
                <p><strong>Lưu ý:</strong> Do không có email server, link được hiển thị trực tiếp dưới đây:</p>
                <div class="reset-link">
                    <a href="${pageContext.request.contextPath}${resetLink}">
                        ${pageContext.request.contextPath}${resetLink}
                    </a>
                </div>
                <p style="margin-top: 10px; font-size: 0.85rem; color: #666;">
                    <i class="fas fa-clock"></i> Link có hiệu lực trong 15 phút
                </p>
            </div>
        </c:if>

        <!-- Hiển thị thông báo lỗi -->
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="<%= request.getContextPath() %>/ForgotPasswordServlet" method="post">
            <!-- CSRF Token -->
            <input type="hidden" name="_csrf_token" value="${_csrf_token}">
            
            <div class="form-group">
                <label for="email">Email đã đăng ký</label>
                <input type="email" id="email" name="email" required 
                       placeholder="you@example.com"
                       value="<c:out value='${email}'/>">
            </div>

            <button type="submit" class="btn-primary">
                <i class="fas fa-paper-plane"></i> Gửi link khôi phục
            </button>
        </form>
    </div>

    <div class="auth-footer">
        Nhớ mật khẩu rồi? <a href="${pageContext.request.contextPath}/LoginServlet">Đăng nhập ngay</a>
    </div>
</div>

</body>
</html>