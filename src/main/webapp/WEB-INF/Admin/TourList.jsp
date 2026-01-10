<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-map-marked-alt"></i> Quản lý Tour</h1>
        <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=add" class="btn btn-primary">
            <i class="fas fa-plus"></i> Thêm Tour mới
        </a>
    </div>
    
    <!-- Search & Filter -->
    <div class="framed-filter">
        <form action="${pageContext.request.contextPath}/Admin/TourServlet" method="get" class="framed-filter-row">
            <input type="hidden" name="action" value="list">
            <div class="framed-filter-group">
                <input type="text" name="search" placeholder="Tìm kiếm tour..." 
                       value="${param.search}">
            </div>
            <div class="framed-filter-group">
                <button type="submit" class="btn btn-secondary">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
        </form>
    </div>
    
    <!-- Tour Table with Border Frame -->
    <div class="border-frame">
        <div class="border-frame-header">
            <h3><i class="fas fa-list"></i> Danh sách Tour</h3>
        </div>
        <div class="border-frame-body">
            <table class="border-table">
                <thead>
                    <tr>
                        <th style="width: 60px;">ID</th>
                        <th>Tên Tour</th>
                        <th>Điểm đến</th>
                        <th style="width: 100px;">Thời gian</th>
                        <th style="width: 120px;">Giá</th>
                        <th style="width: 100px;">Số chỗ</th>
                        <th style="width: 120px;">Trạng thái</th>
                        <th style="width: 140px;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="tour" items="${tours}">
                        <tr>
                            <td class="text-center">${tour.tourId}</td>
                            <td style="text-align: center;">${tour.name}</td>
                            <td style="text-align: center;">${tour.destination}</td>
                            <td class="text-center">${tour.duration} ngày</td>
                            <td class="text-right">${tour.formattedPrice}</td>
                            <td class="text-center">${tour.availableSlots}</td>
                            <td class="text-center">
                                <span class="status-badge ${tour.status == 'ACTIVE' ? 'active' : 'inactive'}">
                                    ${tour.status == 'ACTIVE' ? 'Hoạt động' : 'Ngừng'}
                                </span>
                            </td>
                            <td class="text-center">
                                <div class="action-buttons">
                                    <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=edit&id=${tour.tourId}" 
                                       class="btn-action edit" title="Sửa">
                                        <i class="fas fa-edit"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=delete&id=${tour.tourId}" 
                                       class="btn-action delete" title="Xóa"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa tour này không?');">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty tours}">
                        <tr>
                            <td colspan="8" class="center-content" style="padding: 50px;">
                                <div class="framed-empty">
                                    <div class="framed-empty-icon">
                                        <i class="fas fa-plane-slash"></i>
                                    </div>
                                    <p class="framed-empty-text">Chưa có tour nào.</p>
                                    <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=add" 
                                       class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Thêm tour đầu tiên
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />