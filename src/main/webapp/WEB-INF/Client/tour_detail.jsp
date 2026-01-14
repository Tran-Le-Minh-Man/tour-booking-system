<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>${not empty tour ? tour.name : 'Chi Tiết Tour'} - Vietnam Travel</title>

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Pacifico&display=swap"
	rel="stylesheet">

<!-- Icon -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/CSS/HomePage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/tour_list.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/tour_detail.css" rel="stylesheet">
</head>
<body>
	<!-- ========== IMPORT HEADER ========== -->
	<jsp:include page="/WEB-INF/Common/header.jsp" />

	<!-- Breadcrumb -->
	<section class="breadcrumb-section">
		<div class="container">
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/"><i class="fas fa-home"></i>
							Trang chủ</a></li>
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/TourListServlet">Danh sách
							tour</a></li>
					<li class="breadcrumb-item active" aria-current="page">${not empty tour ? tour.name : 'Chi Tiết Tour'}</li>
				</ol>
			</nav>
		</div>
	</section>

	<!-- Tour Detail Section -->
	<section class="tour-detail-section">
		<div class="container">
			<c:choose>
				<c:when test="${not empty tour}">
					<div class="tour-detail-layout">
						<!-- Left Column: Tour Information -->
						<div class="tour-detail-main">
							<!-- Tour Image -->
							<div class="tour-detail-image">
								<c:choose>
									<c:when test="${not empty tour.imageUrl}">
										<img src="${pageContext.request.contextPath}/${tour.imageUrl}"
											alt="${tour.name}"
											onerror="this.onerror=null;this.src='https://via.placeholder.com/900x500?text=${fn:replace(tour.name,' ','+')}'">
									</c:when>
									<c:otherwise>
										<img
											src="https://via.placeholder.com/900x500?text=${fn:replace(tour.name,' ','+')}"
											alt="${tour.name}">
									</c:otherwise>
								</c:choose>
								<c:if test="${tour.availableSlots <= 5 && tour.availableSlots > 0}">
									<div class="image-badge badge-warning">
										<i class="fas fa-fire"></i> Còn ${tour.availableSlots} chỗ
									</div>
								</c:if>
								<c:if test="${tour.availableSlots == 0}">
									<div class="image-badge badge-danger">
										<i class="fas fa-times"></i> Hết chỗ
									</div>
								</c:if>
							</div>

							<!-- Tour Info Header -->
							<div class="tour-detail-header">
								<div class="tour-header-top">
									<div class="tour-destination-badge">
										<i class="fas fa-map-marker-alt"></i> ${tour.destination}
									</div>
									<!-- Favorite Button -->
									<c:choose>
										<c:when test="${not empty sessionScope.user}">
											<button type="button" class="btn-favorite ${isFavorite ? 'active' : ''}" 
													data-tour-id="${tour.tourId}"
													title="${isFavorite ? 'Bỏ yêu thích' : 'Thêm vào yêu thích'}">
												<i class="${isFavorite ? 'fas' : 'far'} fa-heart"></i>
												<span class="favorite-text">${isFavorite ? 'Đã thích' : 'Yêu thích'}</span>
											</button>
										</c:when>
										<c:otherwise>
											<a href="${pageContext.request.contextPath}/LoginServlet?redirect=${pageContext.request.requestURL}?tourId=${tour.tourId}" 
											   class="btn-favorite" title="Đăng nhập để yêu thích">
												<i class="far fa-heart"></i>
												<span class="favorite-text">Yêu thích</span>
											</a>
										</c:otherwise>
									</c:choose>
								</div>
								<h1 class="tour-title">${tour.name}</h1>
								
								<div class="tour-meta">
									<div class="meta-item">
										<i class="far fa-calendar-alt"></i> 
										<span>Khởi hành: <strong>${tour.formattedDepartureDate}</strong></span>
									</div>
									<div class="meta-item">
										<i class="fas fa-clock"></i> 
										<span>Thời gian: <strong>${tour.duration} ngày<c:if test="${tour.duration > 1}"> ${tour.duration - 1} đêm</c:if></strong></span>
									</div>
									<div class="meta-item">
										<i class="fas fa-users"></i> 
										<span>Số chỗ: <strong>${tour.availableSlots} / ${tour.maxParticipants}</strong></span>
									</div>
								</div>
							</div>

							<!-- Tour Description -->
							<div class="tour-content">
								<h2><i class="fas fa-info-circle"></i> Giới thiệu tour</h2>
								<div class="description-text">
									<c:choose>
										<c:when test="${not empty tour.description}">
											<p>${fn:replace(tour.description, newline, '<br>')}</p>
										</c:when>
										<c:otherwise>
											<p>Tour du lịch đến ${tour.destination} là lựa chọn hoàn hảo cho những ai yêu thích khám phá và trải nghiệm văn hóa, cảnh đẹp của Việt Nam. Hành trình được thiết kế để mang đến những trải nghiệm tuyệt vời nhất cho du khách.</p>
										</c:otherwise>
									</c:choose>
								</div>
							</div>

							<!-- Tour Schedule -->
							<div class="tour-schedule">
								<h2><i class="fas fa-list-ol"></i> Lịch trình dự kiến</h2>
								<div class="schedule-content">
									<div class="schedule-item">
										<div class="schedule-day">Ngày 1</div>
										<div class="schedule-info">
											<h4>Khởi hành & Di chuyển</h4>
											<p>Đoàn tập trung tại điểm hẹn, khởi hành đi ${tour.destination}. Trên đường đi, hướng dẫn viên sẽ giới thiệu về lịch trình và các điểm đến. Ăn trưa và nghỉ ngơi tại nhà hàng địa phương.</p>
										</div>
									</div>
									<div class="schedule-item">
										<div class="schedule-day">Ngày ${tour.duration > 1 ? '2' : 'cuối'}</div>
										<div class="schedule-info">
											<h4>Khám phá ${tour.destination}</h4>
											<p>Tham quan các điểm du lịch nổi tiếng tại ${tour.destination}. Du khách sẽ được chiêm ngưỡng cảnh đẹp thiên nhiên, tìm hiểu văn hóa địa phương và thưởng thức ẩm thực đặc sản.</p>
										</div>
									</div>
									<c:if test="${tour.duration > 2}">
										<div class="schedule-item">
											<div class="schedule-day">Ngày 3</div>
											<div class="schedule-info">
												<h4>Tiếp tục khám phá</h4>
												<p>Tiếp tục hành trình khám phá những điểm đến còn lại. Tham quan các danh lam thắng cảnh và có thời gian tự do cho du khách.</p>
											</div>
										</div>
									</c:if>
									<c:if test="${tour.duration > 3}">
										<div class="schedule-item">
											<div class="schedule-day">Ngày ${tour.duration}</div>
											<div class="schedule-info">
												<h4>Trở về</h4>
												<p>Ăn sáng, thanh toán và trả phòng. Đoàn khởi hành trở về điểm xuất phát, kết thúc hành trình tour du lịch ${tour.destination}.</p>
											</div>
										</div>
									</c:if>
								</div>
							</div>

							<!-- Tour Policy -->
							<div class="tour-policy">
								<h2><i class="fas fa-shield-alt"></i> Chính sách tour</h2>
								<div class="policy-content">
									<div class="policy-item">
										<h4><i class="fas fa-ban"></i> Bao gồm</h4>
										<ul>
											<li>Vé tham quan theo lịch trình</li>
											<li>Hướng dẫn viên tiếng Việt nhiệt tình</li>
											<li>Bảo hiểm du lịch</li>
											<li>Xe ô tô du lịch điều hòa</li>
											<li>Khách sạn 3-4 sao (2 người/phòng)</li>
											<li>Các bữa ăn theo chương trình</li>
										</ul>
									</div>
									<div class="policy-item">
										<h4><i class="fas fa-exclamation-triangle"></i> Không bao gồm</h4>
										<ul>
											<li>Chi phí cá nhân</li>
											<li>Thuế VAT</li>
											<li>Tip hướng dẫn viên và tài xế</li>
											<li>Các dịch vụ không có trong chương trình</li>
										</ul>
									</div>
								</div>
							</div>
						</div>

						<!-- Right Column: Booking Box -->
						<aside class="tour-detail-sidebar">
							<div class="booking-card">
								<div class="booking-price">
									<span class="price-label">Giá tour</span>
									<div class="price-value">
										<c:choose>
											<c:when test="${tour.price != null}">
												<fmt:formatNumber value="${tour.price}" type="number"
													pattern="#,###" />đ
											</c:when>
											<c:otherwise>
												Liên hệ
											</c:otherwise>
										</c:choose>
									</div>
									<span class="price-note">/ người lớn</span>
								</div>

								<div class="booking-info">
									<div class="info-row">
										<span class="info-icon"><i class="far fa-calendar-alt"></i></span>
										<span class="info-text">Khởi hành: <strong>${tour.formattedDepartureDate}</strong></span>
									</div>
									<div class="info-row">
										<span class="info-icon"><i class="fas fa-clock"></i></span>
										<span class="info-text">Thời gian: <strong>${tour.duration} ngày</strong></span>
									</div>
									<div class="info-row">
										<span class="info-icon"><i class="fas fa-map-marker-alt"></i></span>
										<span class="info-text">Điểm đến: <strong>${tour.destination}</strong></span>
									</div>
									<div class="info-row">
										<span class="info-icon"><i class="fas fa-users"></i></span>
										<span class="info-text">Còn <strong>${tour.availableSlots}</strong> chỗ</span>
									</div>
								</div>

									<c:choose>
										<c:when test="${tour.availableSlots > 0}">
											<c:choose>
												<c:when test="${isLoggedIn}">
													<form action="${pageContext.request.contextPath}/BookingServlet" method="get" class="booking-form">
														<input type="hidden" name="tourId" value="${tour.tourId}">
														<div class="quantity-selector">
															<label for="quantity">Số người lớn</label>
															<div class="quantity-input">
																<button type="button" class="qty-btn minus">-</button>
																<input type="number" id="quantity" name="quantity" value="1" min="1" max="${tour.availableSlots}">
																<button type="button" class="qty-btn plus">+</button>
															</div>
														</div>
														<button type="submit" class="btn-booking">
															<i class="fas fa-shopping-cart"></i> ĐẶT TOUR NGAY
														</button>
													</form>
												</c:when>
												<c:otherwise>
													<div class="login-notice">
														<i class="fas fa-user-lock"></i>
														<h4>Vui lòng đăng nhập để đặt tour</h4>
														<p>Bạn cần đăng nhập để tiến hành đặt tour này.</p>
														<a href="${pageContext.request.contextPath}/LoginServlet?redirect=${pageContext.request.requestURL}?tourId=${tour.tourId}" class="btn-login-booking">
															<i class="fas fa-sign-in-alt"></i> Đăng nhập ngay
														</a>
													</div>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise>
											<div class="sold-out-notice">
												<i class="fas fa-times-circle"></i>
												<h4>Tour đã hết chỗ</h4>
												<p>Rất tiếc, tour này đã kín chỗ. Vui lòng chọn tour khác hoặc liên hệ để được tư vấn.</p>
											</div>
										</c:otherwise>
									</c:choose>

								<div class="booking-guarantee">
									<div class="guarantee-item">
										<i class="fas fa-check-circle"></i>
										<span>Đặt tour dễ dàng</span>
									</div>
									<div class="guarantee-item">
										<i class="fas fa-shield-alt"></i>
										<span>Bảo mật thông tin</span>
									</div>
									<div class="guarantee-item">
										<i class="fas fa-headset"></i>
										<span>Hỗ trợ 24/7</span>
									</div>
								</div>
							</div>
						</aside>
					</div>

					<!-- Related Tours Section -->
					<c:if test="${not empty relatedTours}">
						<section class="related-tours-section">
							<h2><i class="fas fa-compass"></i> Có thể bạn sẽ thích</h2>
							<div class="related-tours-grid">
								<c:forEach items="${relatedTours}" var="relatedTour">
									<div class="tour-card">
										<div class="tour-image">
											<c:choose>
												<c:when test="${not empty relatedTour.imageUrl}">
													<img src="${pageContext.request.contextPath}/${relatedTour.imageUrl}"
														alt="${relatedTour.name}"
														onerror="this.onerror=null;this.src='https://via.placeholder.com/400x250?text=${fn:replace(relatedTour.destination,' ','+')}'">
												</c:when>
												<c:otherwise>
													<img
														src="https://via.placeholder.com/400x250?text=${fn:replace(relatedTour.destination,' ','+')}"
														alt="${relatedTour.name}">
												</c:otherwise>
											</c:choose>
											<c:if test="${relatedTour.availableSlots <= 5 && relatedTour.availableSlots > 0}">
												<div class="tour-badge badge-warning">
													<i class="fas fa-fire"></i> Còn ${relatedTour.availableSlots} chỗ
												</div>
											</c:if>
										</div>
										<div class="tour-info">
											<div class="tour-destination">
												<i class="fas fa-map-marker-alt"></i> ${relatedTour.destination}
											</div>
											<h3 class="tour-name">
												<a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=${relatedTour.tourId}">${relatedTour.name}</a>
											</h3>
											<div class="tour-details">
												<div class="detail-item">
													<i class="far fa-calendar-alt"></i> <span>${relatedTour.formattedDepartureDate}</span>
												</div>
												<div class="detail-item">
													<i class="fas fa-clock"></i> <span>${relatedTour.duration} ngày</span>
												</div>
												<div class="detail-item">
													<i class="fas fa-users"></i> <span>Còn ${relatedTour.availableSlots} chỗ</span>
												</div>
											</div>
											<div class="tour-footer">
												<div class="tour-price">
													<c:choose>
														<c:when test="${relatedTour.price != null}">
															<fmt:formatNumber value="${relatedTour.price}" type="number"
																pattern="#,###" />đ
														</c:when>
														<c:otherwise>
															Liên hệ
														</c:otherwise>
													</c:choose>
												</div>
												<a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=${relatedTour.tourId}"
													class="btn btn-view">Xem chi tiết</a>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</section>
					</c:if>

				</c:when>
				<c:otherwise>
					<!-- Error State -->
					<div class="error-section">
						<div class="error-content">
							<i class="fas fa-exclamation-triangle"></i>
							<h2>Tour không tồn tại</h2>
							<p>Rất tiếc, tour bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
							<a href="${pageContext.request.contextPath}/TourListServlet" class="btn btn-primary">
								<i class="fas fa-arrow-left"></i> Quay lại danh sách tour
							</a>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</section>

	<!-- ========== IMPORT FOOTER ========== -->
	<jsp:include page="/WEB-INF/Common/footer.jsp" />

	<!-- JavaScript -->
	<script>
		// Quantity selector functionality
		document.addEventListener('DOMContentLoaded', function() {
			const minusBtn = document.querySelector('.qty-btn.minus');
			const plusBtn = document.querySelector('.qty-btn.plus');
			const quantityInput = document.querySelector('#quantity');
			
			if (minusBtn && plusBtn && quantityInput) {
				minusBtn.addEventListener('click', function() {
					let currentValue = parseInt(quantityInput.value);
					if (currentValue > 1) {
						quantityInput.value = currentValue - 1;
					}
				});
				
				plusBtn.addEventListener('click', function() {
					let currentValue = parseInt(quantityInput.value);
					let maxValue = parseInt(quantityInput.max);
					if (currentValue < maxValue) {
						quantityInput.value = currentValue + 1;
					}
				});
			}
			
			// Mobile menu toggle
			document.querySelector('.nav-toggle')?.addEventListener('click', function() {
				document.querySelector('.nav-menu')?.classList.toggle('active');
			});
			
			// Favorite button functionality - with proper event handling to prevent page jump
			const favoriteBtn = document.querySelector('.btn-favorite[data-tour-id]');
			if (favoriteBtn) {
				favoriteBtn.addEventListener('click', function(e) {
					// Stop propagation to prevent any parent form submission
					e.stopPropagation();
					e.preventDefault();
					
					// Check if already processing
					if (this.style.pointerEvents === 'none') {
						return;
					}
					
					const tourId = this.dataset.tourId;
					const isActive = this.classList.contains('active');
					const action = isActive ? 'remove' : 'add';
					const icon = this.querySelector('i');
					const textSpan = this.querySelector('.favorite-text');
					
					// Show loading state
					this.style.opacity = '0.7';
					this.style.pointerEvents = 'none';
					
					// Send AJAX request using POST for better security
					fetch('${pageContext.request.contextPath}/FavoritesServlet', {
						method: 'POST',
						headers: {
							'Content-Type': 'application/x-www-form-urlencoded',
						},
						body: 'action=' + action + '&tourId=' + tourId
					})
						.then(response => {
							// Check if response is OK before parsing JSON
							if (!response.ok) {
								throw new Error('Network response was not ok');
							}
							return response.json();
						})
						.then(data => {
							console.log('Favorites response:', data);
							if (data.status === 'success') {
								// Toggle button state based on response
								if (data.isFavorite === true || data.added === true) {
									this.classList.add('active');
									icon.classList.remove('far');
									icon.classList.add('fas');
									textSpan.textContent = 'Đã thích';
									this.title = 'Bỏ yêu thích';
								} else {
									this.classList.remove('active');
									icon.classList.remove('fas');
									icon.classList.add('far');
									textSpan.textContent = 'Yêu thích';
									this.title = 'Thêm vào yêu thích';
								}
								
								// Show notification
								showNotification(data.message || 'Thao tác thành công!', 'success');
							} else if (data.status === 'error' && data.redirect) {
								// Redirect to login if required
								window.location.href = data.redirect;
							} else {
								showNotification(data.message || 'Có lỗi xảy ra!', 'error');
							}
						})
						.catch(error => {
							console.error('Error:', error);
							showNotification('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
						})
						.finally(() => {
							// Remove loading state
							this.style.opacity = '1';
							this.style.pointerEvents = 'auto';
						});
					});
			}
			
			// Notification function
			function showNotification(message, type) {
				// Create notification element
				const notification = document.createElement('div');
				notification.className = 'notification notification-' + type;
				notification.innerHTML = '<i class="fas ' + (type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle') + '"></i><span>' + message + '</span>';
				
				// Add styles
				notification.style.cssText = 'position: fixed;top: 100px;right: 20px;padding: 15px 25px;border-radius: 8px;color: white;font-weight: 500;display: flex;align-items: center;gap: 10px;z-index: 9999;animation: slideIn 0.3s ease;' + (type === 'success' ? 'background: #27ae60;' : 'background: #e74c3c;');
				
				// Add animation keyframes
				if (!document.getElementById('notification-styles')) {
					const style = document.createElement('style');
					style.id = 'notification-styles';
					style.textContent = '@keyframes slideIn { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } } @keyframes slideOut { from { transform: translateX(0); opacity: 1; } to { transform: translateX(100%); opacity: 0; } }';
					document.head.appendChild(style);
				}
				
				document.body.appendChild(notification);
				
				// Remove after 3 seconds
				setTimeout(() => {
					notification.style.animation = 'slideOut 0.3s ease forwards';
					setTimeout(() => notification.remove(), 300);
				}, 3000);
			}
		});
	</script>
</body>
</html>