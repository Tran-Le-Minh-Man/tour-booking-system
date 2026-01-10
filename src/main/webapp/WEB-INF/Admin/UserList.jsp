<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1><i class="fas fa-users"></i> Quản lý Người dùng</h1>
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
                <i class="fas fa-users"></i>
            </div>
            <div class="stat-info">
                <h3>Tổng người dùng</h3>
                <p class="stat-number">${totalUsers}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon purple">
                <i class="fas fa-user-shield"></i>
            </div>
            <div class="stat-info">
                <h3>Quản trị viên</h3>
                <p class="stat-number">${adminCount}</p>
            </div>
        </div>
        
        <div class="stat-card">
            <div class="stat-icon green">
                <i class="fas fa-user"></i>
            </div>
            <div class="stat-info">
                <h3>Người dùng</h3>
                <p class="stat-number">${userCount}</p>
            </div>
        </div>
    </div>
    
    <!-- Filter Section -->
    <div class="framed-filter">
        <div class="framed-filter-row">
            <div class="framed-filter-group">
                <select name="role" id="roleFilter" onchange="applyUserFilter()">
                    <option value="">Tất cả quyền</option>
                    <option value="ADMIN" ${roleFilter == 'ADMIN' ? 'selected' : ''}>Quản trị viên</option>
                    <option value="USER" ${roleFilter == 'USER' ? 'selected' : ''}>Người dùng</option>
                </select>
            </div>
            <div class="framed-filter-group">
                <input type="text" id="searchTerm" name="search" placeholder="Tìm kiếm theo tên, email..." value="${searchTerm}">
            </div>
            <div class="framed-filter-group">
                <button type="button" class="btn btn-primary" onclick="applyUserFilter()">
                    <i class="fas fa-search"></i> Tìm kiếm
                </button>
            </div>
            <div class="framed-filter-group">
                <a href="${pageContext.request.contextPath}/Admin/UserServlet?action=list" class="btn btn-secondary">
                    <i class="fas fa-sync"></i> Tải lại
                </a>
            </div>
        </div>
    </div>
    
    <!-- Users Table with Border Frame -->
    <div class="border-frame">
        <div class="border-frame-header">
            <h3><i class="fas fa-list"></i> Danh sách Người dùng</h3>
        </div>
        <div class="border-frame-body">
            <table class="border-table">
                <thead>
                    <tr>
                        <th style="width: 60px;">ID</th>
                        <th>Họ và tên</th>
                        <th>Email</th>
                        <th style="width: 130px;">Số điện thoại</th>
                        <th style="width: 130px;">Quyền</th>
                        <th style="width: 150px;">Ngày tạo</th>
                        <th style="width: 180px;">Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="user" items="${users}">
                        <tr>
                            <td class="text-center">${user.userId}</td>
                            <td style="text-align: center;">
                                <strong>${user.fullName}</strong>
                                <c:if test="${user.userId == currentAdminId}">
                                    <span class="badge-info">(Bạn)</span>
                                </c:if>
                            </td>
                            <td style="text-align: center;">${user.email}</td>
                            <td class="text-center">${user.phone}</td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${user.role == 'ADMIN'}">
                                        <span class="status-badge active">
                                            <i class="fas fa-shield-alt"></i> Admin
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-badge inactive">
                                            <i class="fas fa-user"></i> User
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">${user.createdAt}</td>
                            <td class="text-center">
                                <c:if test="${user.userId != currentAdminId}">
                                    <!-- Change Role Dropdown -->
                                    <form action="${pageContext.request.contextPath}/Admin/UserServlet" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="updateRole">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <select name="role" onchange="this.form.submit()" class="form-control" style="padding: 8px 12px; min-width: 100px;">
                                            <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>User</option>
                                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                                        </select>
                                    </form>
                                    
                                    <!-- Delete Button -->
                                    <a href="${pageContext.request.contextPath}/Admin/UserServlet?action=delete&id=${user.userId}" 
                                       class="btn-action delete" title="Xóa"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa người dùng này không?\nLưu ý: Tất cả đặt tour của người dùng này cũng sẽ bị xóa.');">
                                        <i class="fas fa-trash"></i>
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="7" class="center-content" style="padding: 50px;">
                                <div class="framed-empty">
                                    <div class="framed-empty-icon">
                                        <i class="fas fa-users-slash"></i>
                                    </div>
                                    <p class="framed-empty-text">Chưa có người dùng nào.</p>
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
function applyUserFilter() {
    var role = document.getElementById('roleFilter').value;
    var search = document.getElementById('searchTerm').value;
    var url = '${pageContext.request.contextPath}/Admin/UserServlet?action=list';
    
    if (role !== '') {
        url += '&role=' + encodeURIComponent(role);
    }
    
    if (search.trim() !== '') {
        url += '&search=' + encodeURIComponent(search.trim());
    }
    
    window.location.href = url;
}

// Enter key to search
document.getElementById('searchTerm').addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        applyUserFilter();
    }
});
</script>

<jsp:include page="../Common/AdminFooter.jsp" />