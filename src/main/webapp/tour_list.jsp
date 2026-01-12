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
<title>Tất Cả Tour Du Lịch - Việt Nam Travel</title>

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Pacifico&display=swap"
	rel="stylesheet">

<!-- Icon -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link href="CSS/HomePage.css" rel="stylesheet">
<link href="CSS/tour_list.css" rel="stylesheet">
</head>
<body>
	<!-- ========== IMPORT HEADER ========== -->
	<jsp:include page="WEB-INF/Common/header.jsp" />

	<!-- Page Header -->
	<section class="page-header">
		<div class="container">
			<h1>Khám Phá Các Tour Du Lịch</h1>
			<p class="lead">Hành trình tuyệt vời chờ đợi bạn</p>
		</div>
	</section>

	<!-- Main Content -->
	<section class="tour-list-section">
		<div class="container">
			<div class="tour-list-layout">
				<!-- Sidebar Filters -->
				<aside class="filters-sidebar">
					<div class="filter-card">
						<h3>
							<i class="fas fa-filter"></i> Bộ Lọc Tìm Kiếm
						</h3>

						<form action="${pageContext.request.contextPath}/TourListServlet"
							method="get" class="filter-form">

							<!-- Search by keyword -->
							<div class="filter-group">
								<label for="keyword">Từ khóa</label>
								<div class="search-input-wrapper">
									<i class="fas fa-search"></i> <input type="text" id="keyword"
										name="keyword" placeholder="Tên tour, địa điểm..."
										value="${searchKeyword}">
								</div>
							</div>

							<!-- Destination filter -->
							<div class="filter-group">
								<label for="destination">Điểm đến</label> <select
									id="destination" name="destination">
									<option value="all">Tất cả điểm đến</option>
									<c:forEach items="${destinations}" var="dest">
										<option value="${dest}"
											${selectedDestination == dest ? 'selected' : ''}>${dest}</option>
									</c:forEach>
								</select>
							</div>

							<!-- Departure Date filter -->
							<div class="filter-group">
								<label for="departure_date">Ngày khởi hành</label> <input
									type="date" id="departure_date" name="departure_date"
									value="${departureDate}">
							</div>

							<!-- Price range filter -->
							<div class="filter-group">
								<label>Khoảng giá</label>
								<div class="price-inputs">
									<input type="number" name="minPrice" placeholder="Tối thiểu"
										value="${selectedMinPrice}" min="0"> <span>-</span> <input
										type="number" name="maxPrice" placeholder="Tối đa"
										value="${selectedMaxPrice}" min="0">
								</div>
							</div>

							<!-- Duration filter -->
							<div class="filter-group">
								<label for="duration">Thời gian</label> <select id="duration"
									name="duration">
									<option value="all">Tất cả</option>
									<option value="1" ${selectedDuration == '1' ? 'selected' : ''}>1
										ngày</option>
									<option value="2" ${selectedDuration == '2' ? 'selected' : ''}>2
										ngày 1 đêm</option>
									<option value="3" ${selectedDuration == '3' ? 'selected' : ''}>3
										ngày 2 đêm</option>
									<option value="4" ${selectedDuration == '4' ? 'selected' : ''}>4
										ngày 3 đêm</option>
									<option value="5" ${selectedDuration == '5' ? 'selected' : ''}>5
										ngày 4 đêm</option>
									<option value="7" ${selectedDuration == '7' ? 'selected' : ''}>Trên
										5 ngày</option>
								</select>
							</div>

							<!-- Sort by -->
							<div class="filter-group">
								<label for="sortBy">Sắp xếp theo</label> <select id="sortBy"
									name="sortBy">
									<option value="">Mặc định</option>
									<option value="price_asc"
										${selectedSortBy == 'price_asc' ? 'selected' : ''}>Giá:
										Thấp đến cao</option>
									<option value="price_desc"
										${selectedSortBy == 'price_desc' ? 'selected' : ''}>Giá:
										Cao đến thấp</option>
									<option value="date_asc"
										${selectedSortBy == 'date_asc' ? 'selected' : ''}>Ngày
										khởi hành: Sớm nhất</option>
									<option value="date_desc"
										${selectedSortBy == 'date_desc' ? 'selected' : ''}>Ngày
										khởi hành: Mới nhất</option>
									<option value="duration_asc"
										${selectedSortBy == 'duration_asc' ? 'selected' : ''}>Thời
										gian: Ngắn nhất</option>
									<option value="duration_desc"
										${selectedSortBy == 'duration_desc' ? 'selected' : ''}>Thời
										gian: Dài nhất</option>
								</select>
							</div>

							<!-- Filter buttons -->
							<div class="filter-buttons">
								<button type="submit" class="btn btn-filter">
									<i class="fas fa-search"></i> Áp dụng
								</button>
								<a href="${pageContext.request.contextPath}/TourListServlet"
									class="btn btn-reset"> <i class="fas fa-redo"></i> Đặt lại
								</a>
							</div>
						</form>
					</div>
				</aside>

				<!-- Tour Grid -->
				<main class="tours-main">
					<!-- Results header -->
					<div class="results-header">
						<div class="results-count">
							<c:choose>
								<c:when test="${empty tours}">
									<p>
										<i class="fas fa-info-circle"></i> Không tìm thấy tour nào phù
										hợp
									</p>
								</c:when>
								<c:otherwise>
									<p>
										<i class="fas fa-list"></i> Tìm thấy <strong>${fn:length(tours)}</strong>
										tour du lịch
									</p>
								</c:otherwise>
							</c:choose>
						</div>
						<c:if test="${not empty searchKeyword}">
							<div class="search-info">
								<span>Kết quả cho: "<strong>${searchKeyword}</strong>"
								</span>
							</div>
						</c:if>
					</div>

					<!-- Tours Grid -->
					<c:choose>
						<c:when test="${empty tours}">
							<div class="no-results">
								<i class="fas fa-search"></i>
								<h3>Không tìm thấy tour phù hợp</h3>
								<p>Hãy thử điều chỉnh bộ lọc hoặc tìm kiếm với từ khóa khác</p>
								<a href="${pageContext.request.contextPath}/TourListServlet"
									class="btn btn-primary">Xem tất cả tour</a>
							</div>
						</c:when>
						<c:otherwise>
							<div class="tours-grid">
								<c:forEach items="${tours}" var="tour">
									<div class="tour-card">
										<div class="tour-image">
											<c:choose>
												<c:when test="${not empty tour.imageUrl}">
													<img src="${tour.imageUrl}" alt="${tour.name}"
														onerror="this.src='https://via.placeholder.com/400x300?text=${fn:replace(tour.destination,' ','+')}'">
												</c:when>
												<c:otherwise>
													<img
														src="https://via.placeholder.com/400x300?text=${fn:replace(tour.destination,' ','+')}"
														alt="${tour.name}">
												</c:otherwise>
											</c:choose>
											<c:if
												test="${tour.availableSlots() <= 5 && tour.availableSlots() > 0}">
												<div class="tour-badge badge-warning">
													<i class="fas fa-fire"></i> Còn ${tour.availableSlots()}
													chỗ
												</div>
											</c:if>
											<c:if test="${tour.availableSlots() == 0}">
												<div class="tour-badge badge-danger">
													<i class="fas fa-times"></i> Hết chỗ
												</div>
											</c:if>
										</div>
										<div class="tour-info">
											<div class="tour-destination">
												<i class="fas fa-map-marker-alt"></i> ${tour.destination}
											</div>
											<h3 class="tour-name">
												<a href="TourDetailServlet?tourId=${tour.tourId}">${tour.name}</a>
											</h3>
											<div class="tour-details">
												<div class="detail-item">
													<i class="far fa-calendar-alt"></i> <span>Khởi hành:
														${tour.formattedDepartureDate}</span>
												</div>
												<div class="detail-item">
													<i class="fas fa-clock"></i> <span>${tour.duration}
														ngày ${tour.duration > 1 ? '' : ''}</span>
												</div>
												<div class="detail-item">
													<i class="fas fa-users"></i> <span>Còn
														${tour.availableSlots()} chỗ</span>
												</div>
											</div>
											<div class="tour-footer">
												<div class="tour-price">
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
												<a href="TourDetailServlet?tourId=${tour.tourId}"
													class="btn btn-view">Xem chi tiết</a>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:otherwise>
					</c:choose>

					<!-- Pagination (optional for future) -->
					<c:if test="${fn:length(tours) >= 9}">
						<div class="pagination">
							<a href="#" class="btn btn-page active">1</a> <a href="#"
								class="btn btn-page">2</a> <a href="#" class="btn btn-page">3</a>
							<a href="#" class="btn btn-page"><i
								class="fas fa-chevron-right"></i></a>
						</div>
					</c:if>
				</main>
			</div>
		</div>
	</section>

	<!-- ========== IMPORT FOOTER ========== -->
	<jsp:include page="WEB-INF/Common/footer.jsp" />

	<!-- JavaScript -->
	<script>
		// Mobile menu toggle
		document.querySelector('.nav-toggle').addEventListener(
				'click',
				function() {
					document.querySelector('.nav-menu').classList
							.toggle('active');
				});

		// Auto-submit form on select change
		document.querySelectorAll('.filter-form select').forEach(
				function(select) {
					select.addEventListener('change', function() {
						this.form.submit();
					});
				});
	</script>
</body>
</html>
