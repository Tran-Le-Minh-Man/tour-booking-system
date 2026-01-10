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
    
    <!-- Recent Orders Section with Border Frame -->
    <div class="border-frame">
        <div class="border-frame-header">
            <h3><i class="fas fa-clock"></i> Đơn đặt tour gần đây</h3>
        </div>
        <div class="border-frame-body">
            <c:if test="${not empty recentBookings}">
                <div class="recent-orders">
                    <c:forEach var="booking" items="${recentBookings}">
                        <div class="framed-item">
                            <div class="framed-item-content">
                                <div class="framed-item-title">${booking.userName}</div>
                                <div class="framed-item-subtitle">
                                    <span><i class="fas fa-map-marker-alt"></i> ${booking.tourDestination}</span>
                                    <span><i class="fas fa-users"></i> ${booking.numParticipants} người</span>
                                </div>
                            </div>
                            <div class="framed-item-action">
                                <span style="font-weight: 600; color: #2ecc71; margin-right: 10px;">${booking.formattedPrice}</span>
                                <span class="order-status ${booking.status == 'CONFIRMED' ? 'confirmed' : booking.status == 'PENDING' ? 'pending' : 'cancelled'}">
                                    ${booking.formattedStatus}
                                </span>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
            
            <c:if test="${empty recentBookings}">
                <div class="framed-empty">
                    <div class="framed-empty-icon">
                        <i class="fas fa-calendar-times"></i>
                    </div>
                    <p class="framed-empty-text">Chưa có đơn đặt tour nào gần đây.</p>
                </div>
            </c:if>
            
            <div style="text-align: center; margin-top: 20px;">
                <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=list" class="btn btn-primary">
                    <i class="fas fa-list"></i> Xem tất cả đơn đặt tour
                </a>
            </div>
        </div>
    </div>
    
    <!-- Quick Actions -->
    <div class="dashboard-section" style="margin-top: 25px;">
        <h2><i class="fas fa-bolt"></i> Thao tác nhanh</h2>
        <div class="quick-actions">
            <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=add" class="action-btn">
                <i class="fas fa-plus-circle"></i> Thêm Tour mới
            </a>
            <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=list" class="action-btn">
                <i class="fas fa-list"></i> Xem đơn đặt tour
            </a>
        </div>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />