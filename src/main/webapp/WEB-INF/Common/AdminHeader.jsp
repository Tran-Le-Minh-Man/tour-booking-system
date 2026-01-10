<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<c:set var="current" value="${pageContext.request.requestURI}" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Tour Booking System</title>
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/CSS/Admin.css" rel="stylesheet">
</head>
<body>
    <div class="admin-container">
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2><i class="fas fa-plane"></i> Admin Panel</h2>
            </div>
            
            <nav class="sidebar-menu">
                <ul>
                    <li>
                        <a href="${pageContext.request.contextPath}/Admin/Dashboard"
                           class="${fn:contains(current, 'AdminDashboard.jsp') ? 'active' : ''}">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/TourAdminServlet?action=list"
                           class="${fn:contains(current, 'TourList.jsp') ? 'active' : ''}">
                            <i class="fas fa-map-marked-alt"></i> Quản lý Tour
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/BookingAdminServlet?action=list"
                           class="${fn:contains(current, 'BookingList.jsp') ? 'active' : ''}">
                            <i class="fas fa-calendar-check"></i> Quản lý Đặt tour
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/HomePage.jsp">
                            <i class="fas fa-arrow-left"></i> Quay lại Website
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/LogoutServlet">
                            <i class="fas fa-sign-out-alt"></i> Đăng xuất
                        </a>
                    </li>
                </ul>
            </nav>
        </aside>
        
        <main class="main-content">