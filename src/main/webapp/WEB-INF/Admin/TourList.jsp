<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-map-marked-alt"></i> Quản lý Tour</h1>
        <a href="${pageContext.request.contextPath}/TourAdminServlet?action=add" class="btn btn-primary">
            <i class="fas fa-plus"></i> Thêm Tour mới
        </a>
    </div>
    
    <!-- Search & Filter -->
    <div class="filter-section">
        <form action="${pageContext.request.contextPath}/TourAdminServlet" method="get" class="filter-form">
            <input type="hidden" name="action" value="list">
            <input type="text" name="search" placeholder="Tìm kiếm tour..." 
                   value="${param.search}">
            <button type="submit" class="btn btn-secondary">
                <i class="fas fa-search"></i> Tìm kiếm
            </button>
        </form>
    </div>
    
    <!-- Tour Table -->
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên Tour</th>
                    <th>Điểm đến</th>
                    <th>Thời gian</th>
                    <th>Giá</th>
                    <th>Số chỗ</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="tour" items="${tours}">
                    <tr>
                        <td>${tour.tourId}</td>
                        <td>${tour.tourName}</td>
                        <td>${tour.destination}</td>
                        <td>${tour.duration}</td>
                        <td>${tour.price}</td>
                        <td>${tour.availableSlots}</td>
                        <td>
                            <span class="status-badge ${tour.active ? 'active' : 'inactive'}">
                                ${tour.active ? 'Hoạt động' : 'Ngừng'}
                            </span>
                        </td>
                        <td class="actions">
                            <a href="${pageContext.request.contextPath}/TourAdminServlet?action=edit&id=${tour.tourId}" 
                               class="btn btn-sm btn-edit" title="Sửa">
                                <i class="fas fa-edit"></i>
                            </a>
                            <a href="${pageContext.request.contextPath}/TourAdminServlet?action=delete&id=${tour.tourId}" 
                               class="btn btn-sm btn-delete" title="Xóa"
                               onclick="return confirmDelete('Bạn có chắc chắn muốn xóa tour này không?');">
                                <i class="fas fa-trash"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty tours}">
                    <tr>
                        <td colspan="8" style="text-align: center; padding: 30px;">
                            <p>Chưa có tour nào.</p>
                            <a href="${pageContext.request.contextPath}/TourAdminServlet?action=add" 
                               class="btn btn-primary" style="margin-top: 10px;">
                                Thêm tour đầu tiên
                            </a>
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />