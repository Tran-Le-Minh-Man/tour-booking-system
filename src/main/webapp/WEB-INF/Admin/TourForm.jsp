<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="../Common/AdminHeader.jsp" />

<div class="admin-page">
    <div class="border-frame">
        <div class="page-header">
            <h1>
                <i class="fas ${empty tour ? 'fa-plus-circle' : 'fa-edit'}"></i> 
                ${empty tour ? 'Thêm Tour mới' : 'Chỉnh sửa Tour'}
            </h1>
            <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=list" class="btn btn-secondary">
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
            <form action="${pageContext.request.contextPath}/Admin/TourServlet" method="post" class="admin-form">
                
                <input type="hidden" name="action" value="${empty tour ? 'insert' : 'update'}">
                <c:if test="${not empty tour}">
                    <input type="hidden" name="tourId" value="${tour.tourId}">
                </c:if>
                
                <!-- Section: Thông tin cơ bản -->
                <div class="border-table">
                    <div class="section-header">
                        <i class="fas fa-info-circle"></i>
                        <span>Thông tin cơ bản</span>
                    </div>
                    
                    <div class="form-grid">
                        <!-- Tour Name -->
                        <div class="form-group full-width">
                            <label for="name">Tên Tour <span class="required">*</span></label>
                            <input type="text" id="name" name="name" 
                                   value="${tour.name}" required
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
                            <label for="duration">Thời gian (ngày) <span class="required">*</span></label>
                            <input type="number" id="duration" name="duration" 
                                   value="${tour.duration}" required min="1"
                                   placeholder="Số ngày">
                        </div>
                        
                        <!-- Price -->
                        <div class="form-group">
                            <label for="price">Giá (VNĐ) <span class="required">*</span></label>
                            <input type="number" id="price" name="price" 
                                   value="${tour.price}" required min="0"
                                   placeholder="Nhập giá tour...">
                        </div>
                        
                        <!-- Max Participants -->
                        <div class="form-group">
                            <label for="maxParticipants">Số chỗ tối đa <span class="required">*</span></label>
                            <input type="number" id="maxParticipants" name="maxParticipants" 
                                   value="${tour.maxParticipants}" required min="1"
                                   placeholder="Số lượng chỗ">
                        </div>
                        
                        <!-- Departure Date -->
                        <div class="form-group">
                            <label for="departureDate">Ngày khởi hành</label>
                            <input type="date" id="departureDate" name="departureDate" 
                                   value="${tour.formattedDepartureDate}">
                        </div>
                    </div>
                </div>
                
                <!-- Section: Mô tả chi tiết -->
                <div class="border-table">
                    <div class="section-header">
                        <i class="fas fa-align-left"></i>
                        <span>Mô tả chi tiết</span>
                    </div>
                    
                    <div class="form-grid">
                        <!-- Description -->
                        <div class="form-group full-width">
                            <label for="description">Mô tả Tour</label>
                            <textarea id="description" name="description" rows="5"
                                      placeholder="Nhập mô tả chi tiết về tour...">${tour.description}</textarea>
                        </div>
                    </div>
                </div>
                
                <!-- Section: Hình ảnh và trạng thái -->
                <div class="border-table">
                    <div class="section-header">
                        <i class="fas fa-image"></i>
                        <span>Hình ảnh và trạng thái</span>
                    </div>
                    
                    <div class="form-grid">
                        <!-- Image URL -->
                        <div class="form-group">
                            <label for="imageUrl">Hình ảnh tour</label>
                            <input type="text" id="imageUrl" name="imageUrl" 
                                   value="${tour.imageUrl}" 
                                   placeholder="Nhập URL hình ảnh (VD: images/tour1.jpg)">
                            <c:if test="${not empty tour.imageUrl}">
                                <div class="current-image">
                                    <p>Hình ảnh hiện tại:</p>
                                    <img src="${tour.imageUrl}" alt="Tour Image">
                                    <input type="hidden" name="existingImage" value="${tour.imageUrl}">
                                </div>
                            </c:if>
                        </div>
                        
                        <!-- Status -->
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <div class="status-options">
                                <label class="radio-label">
                                    <input type="radio" name="status" value="ACTIVE" 
                                           ${tour.status == 'ACTIVE' || empty tour.status ? 'checked' : ''}>
                                    <span class="radio-custom"></span>
                                    <i class="fas fa-check-circle active-icon"></i> Hoạt động
                                </label>
                                <label class="radio-label">
                                    <input type="radio" name="status" value="INACTIVE" 
                                           ${tour.status == 'INACTIVE' ? 'checked' : ''}>
                                    <span class="radio-custom"></span>
                                    <i class="fas fa-times-circle inactive-icon"></i> Không hoạt động
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Form Actions -->
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <i class="fas fa-save"></i> ${empty tour ? 'Thêm Tour' : 'Cập nhật'}
                    </button>
                    <a href="${pageContext.request.contextPath}/Admin/TourServlet?action=list" 
                       class="btn btn-secondary">
                        <i class="fas fa-times"></i> Hủy
                    </a>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../Common/AdminFooter.jsp" />