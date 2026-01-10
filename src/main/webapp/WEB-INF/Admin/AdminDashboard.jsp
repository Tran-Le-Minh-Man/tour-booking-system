<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-tachometer-alt"></i> Dashboard</h1>
        <p>Xin chào, <strong>${sessionScope.user.fullName}</strong>!</p>
    </div>
    
    <!-- Stats Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon blue">
                <i class="fas fa-map-marked-alt"></i>
            </div>
            <div class="stat-info">
                <h3>Tổng số Tour</h3>
                <p class="stat-number">0</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon green">
                <i class="fas fa-calendar-check"></i>
            </div>
            <div class="stat-info">
                <h3>Đơn đặt tour</h3>
                <p class="stat-number">0</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon orange">
                <i class="fas fa-clock"></i>
            </div>
            <div class="stat-info">
                <h3>Chờ xác nhận</h3>
                <p class="stat-number">0</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon purple">
                <i class="fas fa-users"></i>
            </div>
            <div class="stat-info">
                <h3>Người dùng</h3>
                <p class="stat-number">0</p>
            </div>
        </div>
    </div>
    
    <!-- Quick Actions -->
    <div class="dashboard-section">
        <h2><i class="fas fa-bolt"></i> Thao tác nhanh</h2>
        <div class="quick-actions">
            <a href="${pageContext.request.contextPath}/TourAdminServlet?action=add" class="action-btn">
                <i class="fas fa-plus-circle"></i> Thêm Tour mới
            </a>
            <a href="${pageContext.request.contextPath}/BookingAdminServlet?action=list" class="action-btn">
                <i class="fas fa-list"></i> Xem đơn đặt tour
            </a>
        </div>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />