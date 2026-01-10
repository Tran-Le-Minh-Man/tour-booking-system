<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-chart-line"></i> Báo cáo Doanh thu</h1>
        <div class="period-selector">
            <!-- SỬA: Đổi action từ /RevenueServlet thành /Admin/RevenueServlet -->
            <form action="${pageContext.request.contextPath}/Admin/RevenueServlet" method="get" class="period-form">
                <select name="period" onchange="this.form.submit()">
                    <option value="today" ${period == 'today' ? 'selected' : ''}>Hôm nay</option>
                    <option value="week" ${period == 'week' ? 'selected' : ''}>Tuần này</option>
                    <option value="month" ${period == 'month' ? 'selected' : ''}>Tháng này</option>
                    <option value="year" ${period == 'year' ? 'selected' : ''}>Năm nay</option>
                </select>
            </form>
        </div>
    </div>
    
    <!-- Date Range Info -->
    <div class="date-range-info">
        <i class="fas fa-calendar-alt"></i> 
        Từ ngày ${startDate} đến ${endDate}
    </div>
    
    <!-- Revenue Stats -->
    <div class="stats-grid">
        <div class="stat-card highlight">
            <div class="stat-icon green large">
                <i class="fas fa-money-bill-wave"></i>
            </div>
            <div class="stat-info">
                <h3>Tổng doanh thu</h3>
                <p class="stat-number large">${totalRevenue}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon blue">
                <i class="fas fa-chart-bar"></i>
            </div>
            <div class="stat-info">
                <h3>Doanh thu kỳ này</h3>
                <p class="stat-number">${periodRevenue}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon green">
                <i class="fas fa-check-circle"></i>
            </div>
            <div class="stat-info">
                <h3>Đơn hoàn thành</h3>
                <p class="stat-number">${completedBookings}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon orange">
                <i class="fas fa-clock"></i>
            </div>
            <div class="stat-info">
                <h3>Đơn chờ xử lý</h3>
                <p class="stat-number">${pendingBookings}</p>
            </div>
        </div>
    </div>
    
    <!-- Additional Stats -->
    <div class="dashboard-section">
        <h2><i class="fas fa-chart-pie"></i> Thống kê Đơn đặt tour</h2>
        <div class="stats-detail-grid">
            <div class="stat-detail-item">
                <span class="stat-label">Tổng số đơn</span>
                <span class="stat-value">${totalBookings}</span>
            </div>
            <div class="stat-detail-item">
                <span class="stat-label">Đã xác nhận</span>
                <span class="stat-value confirmed">${confirmedBookings}</span>
            </div>
            <div class="stat-detail-item">
                <span class="stat-label">Hoàn thành</span>
                <span class="stat-value completed">${completedBookings}</span>
            </div>
            <div class="stat-detail-item">
                <span class="stat-label">Đã hủy</span>
                <span class="stat-value cancelled">${cancelledBookings}</span>
            </div>
        </div>
    </div>
    
    <!-- Monthly Revenue Chart -->
    <div class="dashboard-section">
        <h2><i class="fas fa-chart-area"></i> Doanh thu 6 tháng gần đây</h2>
        <div class="chart-container">
            <div class="bar-chart">
                <c:forEach var="month" items="${monthlyRevenue}">
                    <div class="bar-item">
                        <div class="bar-value">${month.formattedRevenue}</div>
                        <c:set var="barHeight" value="20" />
                        <c:if test="${month.revenue != null}">
                            <c:set var="barHeight" value="${month.revenue.doubleValue() / 1000000 * 10}" />
                            <c:if test="${barHeight < 20}">
                                <c:set var="barHeight" value="20" />
                            </c:if>
                            <c:if test="${barHeight > 200}">
                                <c:set var="barHeight" value="200" />
                            </c:if>
                        </c:if>
                        <div class="bar" style="height: ${barHeight}px;"></div>
                        <div class="bar-label">${month.month}</div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    
    <!-- Export Options -->
    <div class="dashboard-section">
        <h2><i class="fas fa-download"></i> Xuất báo cáo</h2>
        <div class="export-buttons">
            <button class="btn btn-primary" onclick="window.print()">
                <i class="fas fa-print"></i> In báo cáo
            </button>
            <button class="btn btn-secondary" onclick="exportToExcel()">
                <i class="fas fa-file-excel"></i> Xuất Excel
            </button>
        </div>
    </div>
</div>

<style>
/* Additional styles for Revenue page */
.date-range-info {
    background: #e3f2fd;
    padding: 10px 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    color: #1976d2;
    font-weight: 500;
}

.period-form select {
    padding: 10px 15px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1rem;
    cursor: pointer;
}

.stat-card.highlight {
    border: 2px solid #2ecc71;
    background: linear-gradient(135deg, #fff 0%, #e8f5e9 100%);
}

.stat-icon.large {
    width: 70px;
    height: 70px;
    font-size: 2rem;
}

.stat-number.large {
    font-size: 2rem;
}

.stats-detail-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 20px;
}

.stat-detail-item {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 10px;
    text-align: center;
}

.stat-detail-item .stat-label {
    display: block;
    color: #7f8c8d;
    font-size: 0.9rem;
    margin-bottom: 8px;
}

.stat-detail-item .stat-value {
    display: block;
    font-size: 1.8rem;
    font-weight: 700;
    color: #2c3e50;
}

.stat-detail-item .stat-value.confirmed {
    color: #27ae60;
}

.stat-detail-item .stat-value.completed {
    color: #3498db;
}

.stat-detail-item .stat-value.cancelled {
    color: #e74c3c;
}

.chart-container {
    padding: 20px 0;
}

.bar-chart {
    display: flex;
    justify-content: space-around;
    align-items: flex-end;
    height: 250px;
    padding: 20px 0;
    border-bottom: 2px solid #eee;
}

.bar-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
    max-width: 100px;
}

.bar {
    width: 40px;
    background: linear-gradient(180deg, #3498db 0%, #2980b9 100%);
    border-radius: 5px 5px 0 0;
    min-height: 20px;
    transition: height 0.5s ease;
}

.bar:hover {
    background: linear-gradient(180deg, #2ecc71 0%, #27ae60 100%);
}

.bar-value {
    font-size: 0.8rem;
    font-weight: 600;
    color: #2c3e50;
    margin-bottom: 5px;
}

.bar-label {
    font-size: 0.85rem;
    color: #7f8c8d;
    margin-top: 8px;
}

.export-buttons {
    display: flex;
    gap: 15px;
}

@media print {
    .sidebar, .filter-section, .export-buttons, .period-selector {
        display: none !important;
    }
    
    .main-content {
        margin-left: 0 !important;
    }
    
    .admin-container {
        display: block;
    }
}
</style>

<script>
function exportToExcel() {
    // Simple export to CSV
    let csv = 'Tháng,Doanh thu\n';
    <c:forEach var="month" items="${monthlyRevenue}">
    csv += '${month.month},${month.formattedRevenue}\n';
    </c:forEach>
    
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'bao_cao_doanh_thu.csv';
    link.click();
}
</script>

<jsp:include page="../Common/AdminFooter.jsp" />