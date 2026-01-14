<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-calendar-check"></i> Quản lý Đặt tour</h1>
    </div>
    
    <!-- Error/Success Messages -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger">
            <i class="fas fa-exclamation-circle"></i> ${error}
        </div>
    </c:if>
    
    <c:if test="${not empty success}">
        <div class="alert alert-success">
            <i class="fas fa-check-circle"></i> ${success}
        </div>
    </c:if>
    
    <!-- Stats Cards -->
    <div class="stats-grid">
        <div class="stat-card">
            <div class="stat-icon blue">
                <i class="fas fa-list"></i>
            </div>
            <div class="stat-info">
                <h3>Tổng đơn</h3>
                <p class="stat-number">${totalBookings}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon orange">
                <i class="fas fa-clock"></i>
            </div>
            <div class="stat-info">
                <h3>Chờ xác nhận</h3>
                <p class="stat-number">${pendingCount}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon green">
                <i class="fas fa-check"></i>
            </div>
            <div class="stat-info">
                <h3>Đã xác nhận</h3>
                <p class="stat-number">${confirmedCount}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon purple">
                <i class="fas fa-times"></i>
            </div>
            <div class="stat-info">
                <h3>Đã hủy</h3>
                <p class="stat-number">${cancelledCount}</p>
            </div>
        </div>
    </div>
    
    <!-- Filter Section -->
    <div class="framed-filter">
        <div class="framed-filter-row">
            <div class="framed-filter-group">
                <select name="status" id="statusFilter" onchange="applyBookingFilter()">
                    <option value="">Tất cả trạng thái</option>
                    <option value="PENDING" ${statusFilter == 'PENDING' ? 'selected' : ''}>Chờ xác nhận</option>
                    <option value="CONFIRMED" ${statusFilter == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                    <option value="COMPLETED" ${statusFilter == 'COMPLETED' ? 'selected' : ''}>Hoàn thành</option>
                    <option value="CANCELLED" ${statusFilter == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                </select>
            </div>
            <div class="framed-filter-group">
                <input type="text" id="searchTerm" name="search" placeholder="Tìm kiếm..." value="${searchTerm}">
            </div>
            <div class="framed-filter-group">
                <button type="button" class="btn btn-primary" onclick="applyBookingFilter()">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
            <div class="framed-filter-group">
                <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=list" class="btn btn-secondary">
                    <i class="fas fa-sync"></i> Tải lại
                </a>
            </div>
        </div>
    </div>
    
    <!-- Bookings Table with Border Frame -->
    <div class="border-frame">
        <div class="border-frame-header">
            <h3><i class="fas fa-list"></i> Danh sách Đặt tour</h3>
        </div>
        <div class="border-frame-body">
            <table class="border-table">
                <thead>
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Khách hàng</th>
                        <th>Tour</th>
                        <th style="width: 120px;">Ngày đặt</th>
                        <th style="width: 100px;">Số người</th>
                        <th style="width: 140px;">Tổng tiền</th>
                        <th style="width: 130px;">Trạng thái</th>
                        <th style="width: 200px;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="booking" items="${bookings}">
                        <tr>
                            <td class="text-center">#${booking.bookingId}</td>
                            <td style="text-align: center;">
                                <strong>${booking.userName}</strong><br>
                                <small style="color: #6c757d;">${booking.userEmail}</small>
                            </td>
                            <td style="text-align: center;">
                                <strong>${booking.tourName}</strong><br>
                                <small style="color: #6c757d;">${booking.tourDestination}</small>
                            </td>
                            <td class="text-center">${booking.formattedDate}</td>
                            <td class="text-center">${booking.numParticipants}</td>
                            <td class="text-right">${booking.formattedPrice}</td>
                            <td class="text-center">
                                <span class="status-badge status-${booking.status.toLowerCase()}">
                                    ${booking.formattedStatus}
                                </span>
                            </td>
                            <td class="text-center">
                                <c:if test="${booking.isPending()}">
                                    <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=confirm&id=${booking.bookingId}" 
                                       class="btn-action view" title="Xác nhận"
                                       onclick="return confirm('Bạn có chắc chắn muốn xác nhận đặt tour này không?');">
                                        <i class="fas fa-check"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=cancel&id=${booking.bookingId}" 
                                       class="btn-action delete" title="Hủy"
                                       onclick="return confirm('Bạn có chắc chắn muốn hủy đặt tour này không?');">
                                        <i class="fas fa-times"></i>
                                    </a>
                                </c:if>
                                <c:if test="${booking.isConfirmed()}">
                                    <a href="${pageContext.request.contextPath}/Admin/BookingServlet?action=complete&id=${booking.bookingId}" 
                                       class="btn-action edit" title="Hoàn thành"
                                       onclick="return confirm('Đánh dấu đặt tour này là hoàn thành?');">
                                        <i class="fas fa-check-double"></i>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty bookings}">
                        <tr>
                            <td colspan="8" class="center-content" style="padding: 50px;">
                                <div class="framed-empty">
                                    <div class="framed-empty-icon">
                                        <i class="fas fa-calendar-times"></i>
                                    </div>
                                    <p class="framed-empty-text">Chưa có đặt tour nào.</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
function applyBookingFilter() {
    var status = document.getElementById('statusFilter').value;
    var search = document.getElementById('searchTerm').value;
    var url = '${pageContext.request.contextPath}/Admin/BookingServlet?action=list';
    
    if (status !== '') {
        url += '&status=' + encodeURIComponent(status);
    }
    
    if (search.trim() !== '') {
        url += '&search=' + encodeURIComponent(search.trim());
    }
    
    window.location.href = url;
}

// Enter key to search
document.getElementById('searchTerm').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        applyBookingFilter();
    }
});
</script>

<jsp:include page="../Common/AdminFooter.jsp" />