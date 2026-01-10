<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="page-header">
        <h1>
            <i class="fas ${empty tour ? 'fa-plus-circle' : 'fa-edit'}"></i> 
            ${empty tour ? 'Thêm Tour mới' : 'Chỉnh sửa Tour'}
        </h1>
        <a href="${pageContext.request.contextPath}/TourAdminServlet?action=list" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Quay lại
        </a>
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
    
    <!-- Tour Form -->
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/TourAdminServlet" method="post" 
              enctype="multipart/form-data" class="admin-form">
            
            <input type="hidden" name="action" value="${empty tour ? 'insert' : 'update'}">
            <c:if test="${not empty tour}">
                <input type="hidden" name="tourId" value="${tour.tourId}">
            </c:if>
            
            <div class="form-grid">
                <!-- Tour Name -->
                <div class="form-group full-width">
                    <label for="tourName">Tên Tour <span class="required">*</span></label>
                    <input type="text" id="tourName" name="tourName" 
                           value="${tour.tourName}" required
                           placeholder="Nhập tên tour...">
                </div>
                
                <!-- Destination -->
                <div class="form-group">
                    <label for="destination">Điểm đến <span class="required">*</span></label>
                    <input type="text" id="destination" name="destination" 
                           value="${tour.destination}" required
                           placeholder="VD: Phú Quốc, Nha Trang...">
                </div>
                
                <!-- Duration -->
                <div class="form-group">
                    <label for="duration">Thời gian <span class="required">*</span></label>
                    <input type="text" id="duration" name="duration" 
                           value="${tour.duration}" required
                           placeholder="VD: 3 ngày 2 đêm">
                </div>
                
                <!-- Price -->
                <div class="form-group">
                    <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                    <input type="number" id="price" name="price" 
                           value="${tour.price}" required min="0"
                           placeholder="Nhập giá tour...">
                </div>
                
                <!-- Available Slots -->
                <div class="form-group">
                    <label for="availableSlots">Số chỗ <span class="required">*</span></label>
                    <input type="number" id="availableSlots" name="availableSlots" 
                           value="${tour.availableSlots}" required min="1"
                           placeholder="Số lượng chỗ">
                </div>
                
                <!-- Departure Date -->
                <div class="form-group">
                    <label for="departureDate">Ngày khởi hành</label>
                    <input type="date" id="departureDate" name="departureDate" 
                           value="${tour.departureDate}">
                </div>
                
                <!-- Description -->
                <div class="form-group full-width">
                    <label for="description">Mô tả Tour</label>
                    <textarea id="description" name="description" rows="5"
                              placeholder="Nhập mô tả chi tiết về tour...">${tour.description}</textarea>
                </div>
                
                <!-- Image URL -->
                <div class="form-group full-width">
                    <label for="imageUrl">Hình ảnh</label>
                    <input type="file" id="imageUrl" name="imageUrl" accept="image/*">
                    <c:if test="${not empty tour.imageUrl}">
                        <div class="current-image">
                            <p>Hình ảnh hiện tại:</p>
                            <img src="${tour.imageUrl}" alt="Tour Image" style="max-width: 200px;">
                            <input type="hidden" name="existingImage" value="${tour.imageUrl}">
                        </div>
                    </c:if>
                </div>
                
                <!-- Active Status -->
                <div class="form-group">
                    <label>Trạng thái</label>
                    <div class="checkbox-group">
                        <label class="checkbox-label">
                            <input type="checkbox" name="active" ${tour.active ? 'checked' : ''}>
                            Hoạt động
                        </label>
                    </div>
                </div>
            </div>
            
            <!-- Form Actions -->
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> ${empty tour ? 'Thêm Tour' : 'Cập nhật'}
                </button>
                <a href="${pageContext.request.contextPath}/TourAdminServlet?action=list" 
                   class="btn btn-secondary">
                    <i class="fas fa-times"></i> Hủy
                </a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />