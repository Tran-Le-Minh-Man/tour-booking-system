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
<title>Tours Yêu Thích - Vietnam Travel</title>

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Pacifico&display=swap"
	rel="stylesheet">

<!-- Icon -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/CSS/HomePage.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/tour_list.css"
	rel="stylesheet">

<style>
.favorites-header {
	background: linear-gradient(135deg, #0C67B3 0%, #1a8fe3 100%);
	color: white;
	padding: 40px 0;
	text-align: center;
	margin-bottom: 30px;
}

.favorites-header h1 {
	font-size: 2.5rem;
	margin: 0 0 10px 0;
	font-family: 'Pacifico', cursive;
}

.favorites-header p {
	font-size: 1.1rem;
	opacity: 0.9;
	margin: 0;
}

.favorites-count {
	display: inline-block;
	background: rgba(255, 255, 255, 0.2);
	padding: 8px 20px;
	border-radius: 20px;
	margin-top: 15px;
	font-weight: 600;
}

.tour-card {
	position: relative;
}

.tour-card .remove-favorite {
	position: absolute;
	top: 15px;
	right: 15px;
	width: 40px;
	height: 40px;
	background: rgba(231, 76, 60, 0.9);
	color: white;
	border: none;
	border-radius: 50%;
	cursor: pointer;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 1.2rem;
	transition: all 0.3s ease;
	z-index: 10;
	box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
}

.tour-card .remove-favorite:hover {
	background: #c0392b;
	transform: scale(1.1);
}

.empty-favorites {
	text-align: center;
	padding: 80px 20px;
	background: white;
	border-radius: 12px;
	box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.empty-favorites i {
	font-size: 5rem;
	color: #ddd;
	margin-bottom: 20px;
}

.empty-favorites h2 {
	color: #666;
	margin: 0 0 15px 0;
}

.empty-favorites p {
	color: #999;
	margin: 0 0 25px 0;
}

.empty-favorites .btn-primary {
	display: inline-block;
	background: linear-gradient(135deg, #0C67B3 0%, #1a8fe3 100%);
	color: white;
	padding: 14px 30px;
	border-radius: 10px;
	text-decoration: none;
	font-weight: 600;
	transition: all 0.3s ease;
}

.empty-favorites .btn-primary:hover {
	transform: translateY(-2px);
	box-shadow: 0 5px 20px rgba(12, 103, 179, 0.4);
	color: white;
}

.tour-actions {
	display: flex;
	gap: 10px;
	margin-top: 15px;
}

.tour-actions .btn-book {
	flex: 1;
	background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
	color: white;
	border: none;
	padding: 12px;
	border-radius: 8px;
	font-weight: 600;
	cursor: pointer;
	transition: all 0.3s ease;
	text-align: center;
	text-decoration: none;
}

.tour-actions .btn-book:hover {
	transform: translateY(-2px);
	box-shadow: 0 5px 20px rgba(231, 76, 60, 0.4);
	color: white;
}

.tour-actions .btn-details {
	flex: 1;
	background: #f8f9fa;
	color: #0C67B3;
	border: 2px solid #0C67B3;
	padding: 10px;
	border-radius: 8px;
	font-weight: 600;
	cursor: pointer;
	transition: all 0.3s ease;
	text-align: center;
	text-decoration: none;
}

.tour-actions .btn-details:hover {
	background: #0C67B3;
	color: white;
}

.confirm-remove {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.5);
	display: none;
	align-items: center;
	justify-content: center;
	z-index: 9999;
}

.confirm-remove.show {
	display: flex;
}

.confirm-dialog {
	background: white;
	padding: 30px;
	border-radius: 12px;
	text-align: center;
	max-width: 400px;
	margin: 20px;
}

.confirm-dialog i {
	font-size: 3rem;
	color: #e74c3c;
	margin-bottom: 15px;
}

.confirm-dialog h3 {
	margin: 0 0 10px 0;
	color: #333;
}

.confirm-dialog p {
	color: #666;
	margin: 0 0 20px 0;
}

.confirm-dialog .btn-group {
	display: flex;
	gap: 10px;
	justify-content: center;
}

.confirm-dialog .btn-cancel {
	background: #f8f9fa;
	color: #666;
	border: none;
	padding: 12px 25px;
	border-radius: 8px;
	cursor: pointer;
	font-weight: 600;
}

.confirm-dialog .btn-confirm {
	background: #e74c3c;
	color: white;
	border: none;
	padding: 12px 25px;
	border-radius: 8px;
	cursor: pointer;
	font-weight: 600;
}

/* Toast notification */
.toast {
	position: fixed;
	bottom: 30px;
	right: 30px;
	padding: 15px 25px;
	border-radius: 8px;
	color: white;
	font-weight: 500;
	z-index: 10000;
	transform: translateX(150%);
	transition: transform 0.3s ease;
	display: flex;
	align-items: center;
	gap: 10px;
}

.toast.show {
	transform: translateX(0);
}

.toast.success {
	background: #27ae60;
}

.toast.error {
	background: #e74c3c;
}
</style>
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
						href="${pageContext.request.contextPath}/HomePage.jsp"><i
							class="fas fa-home"></i> Trang chủ</a></li>
					<li class="breadcrumb-item active" aria-current="page">Tours
						Yêu Thích</li>
				</ol>
			</nav>
		</div>
	</section>

	<!-- Favorites Section -->
	<section class="favorites-section">
		<div class="container">

			<!-- Header -->
			<div class="favorites-header">
				<h1>
					<i class="fas fa-heart"></i> Tours Yêu Thích
				</h1>
				<p>Những tour du lịch bạn đã lưu lại để khám phá sau này</p>
				<c:if test="${not empty favorites}">
					<div class="favorites-count">
						<i class="fas fa-list"></i> ${fn:length(favorites)} tour yêu thích
					</div>
				</c:if>
			</div>

			<!-- Toast Container -->
			<div id="toast" class="toast">
				<i class="fas fa-check-circle"></i> <span id="toast-message"></span>
			</div>

			<!-- Confirm Remove Dialog -->
			<div id="confirm-remove" class="confirm-remove">
				<div class="confirm-dialog">
					<i class="fas fa-heart-broken"></i>
					<h3>Xóa khỏi yêu thích?</h3>
					<p>Bạn có chắc chắn muốn xóa tour này khỏi danh sách yêu thích?</p>
					<div class="btn-group">
						<button class="btn-cancel" onclick="closeConfirmDialog()">Hủy</button>
						<button class="btn-confirm" id="confirm-yes">Xóa</button>
					</div>
				</div>
			</div>

			<c:choose>
				<c:when test="${not empty favorites}">
					<!-- Favorites Grid -->
					<div class="tours-grid">
						<c:forEach items="${favorites}" var="tour" varStatus="status">
							<div class="tour-card" data-tour-id="${tour.tourId}">
								<!-- Remove Button -->
								<button class="remove-favorite"
									onclick='confirmRemove(${tour.tourId}, "${fn:escapeXml(tour.name)}")'

									title="Xóa khỏi yêu thích"> <i class="fas fa-times"></i>
								</button>

								<!-- Tour Image -->
								<div class="tour-image">
									<c:choose>
										<c:when test="${not empty tour.imageUrl}">
											<img
												src="${pageContext.request.contextPath}/${tour.imageUrl}"
												alt="${tour.name}"
												onerror="this.onerror=null;this.src='https://via.placeholder.com/400x250?text=${fn:replace(tour.destination,' ','+')}'">
										</c:when>
										<c:otherwise>
											<img
												src="https://via.placeholder.com/400x250?text=${fn:replace(tour.destination,' ','+')}"
												alt="${tour.name}">
										</c:otherwise>
									</c:choose>

									<!-- Badges -->
									<c:if
										test="${tour.availableSlots <= 5 && tour.availableSlots > 0}">
										<div class="tour-badge badge-warning">
											<i class="fas fa-fire"></i> Còn ${tour.availableSlots} chỗ
										</div>
									</c:if>
									<c:if test="${tour.availableSlots == 0}">
										<div class="tour-badge badge-danger">
											<i class="fas fa-times"></i> Hết chỗ
										</div>
									</c:if>
								</div>

								<!-- Tour Info -->
								<div class="tour-info">
									<div class="tour-destination">
										<i class="fas fa-map-marker-alt"></i> ${tour.destination}
									</div>

									<h3 class="tour-name">
										<a
											href="${pageContext.request.contextPath}/TourDetailServlet?tourId=${tour.tourId}">${tour.name}</a>
									</h3>

									<div class="tour-details">
										<div class="detail-item">
											<i class="far fa-calendar-alt"></i> <span>${tour.formattedDepartureDate}</span>
										</div>
										<div class="detail-item">
											<i class="fas fa-clock"></i> <span>${tour.duration}
												ngày</span>
										</div>
										<div class="detail-item">
											<i class="fas fa-users"></i> <span>Còn
												${tour.availableSlots} chỗ</span>
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

										<div class="tour-actions">
											<a
												href="${pageContext.request.contextPath}/TourDetailServlet?tourId=${tour.tourId}"
												class="btn-details"> <i class="fas fa-info-circle"></i>
												Chi tiết
											</a>
											<c:if test="${tour.availableSlots > 0}">
												<a
													href="${pageContext.request.contextPath}/BookingServlet?tourId=${tour.tourId}"
													class="btn-book"> <i class="fas fa-shopping-cart"></i>
													Đặt ngay
												</a>
											</c:if>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:when>
				<c:otherwise>
					<!-- Empty State -->
					<div class="empty-favorites">
						<i class="far fa-heart"></i>
						<h2>Chưa có tour yêu thích</h2>
						<p>Hãy khám phá và lưu lại những tour du lịch hấp dẫn nhất!</p>
						<a href="${pageContext.request.contextPath}/TourListServlet"
							class="btn-primary"> <i class="fas fa-compass"></i> Khám phá
							tours
						</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</section>

	<!-- ========== IMPORT FOOTER ========== -->
	<jsp:include page="/WEB-INF/Common/footer.jsp" />

	<!-- JavaScript -->
	<script>
        let tourToRemove = null;
        
        // Confirm remove dialog
        function confirmRemove(tourId, tourName) {
            tourToRemove = tourId;
            document.getElementById('confirm-remove').classList.add('show');
            document.getElementById('confirm-yes').onclick = function() {
                removeFromFavorites(tourId);
                closeConfirmDialog();
            };
        }
        
        function closeConfirmDialog() {
            document.getElementById('confirm-remove').classList.remove('show');
            tourToRemove = null;
        }
        
        // Remove from favorites via AJAX
        function removeFromFavorites(tourId) {
            fetch('${pageContext.request.contextPath}/FavoritesServlet?action=remove&tourId=' + tourId, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                if (data.status === 'success') {
                    // Remove card from UI with animation
                    const card = document.querySelector('.tour-card[data-tour-id="' + tourId + '"]');
                    if (card) {
                        card.style.transition = 'all 0.3s ease';
                        card.style.transform = 'scale(0.8)';
                        card.style.opacity = '0';
                        setTimeout(() => {
                            card.remove();
                            updateFavoritesCount();
                            checkEmptyState();
                        }, 300);
                    }
                    showToast('Đã xóa khỏi danh sách yêu thích', 'success');
                } else {
                    showToast('Có lỗi xảy ra: ' + data.message, 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('Có lỗi xảy ra. Vui lòng thử lại.', 'error');
            });
        }
        
        // Update favorites count in header
        function updateFavoritesCount() {
            fetch('${pageContext.request.contextPath}/FavoritesServlet?action=count', {
                method: 'GET'
            })
            .then(response => response.json())
            .then(data => {
                const countElement = document.querySelector('.nav-menu .fa-heart').closest('a');
                if (countElement) {
                    if (data.count > 0) {
                        countElement.innerHTML = '<i class="fas fa-heart"></i> Yêu thích <span class="badge">' + data.count + '</span>';
                    } else {
                        countElement.innerHTML = '<i class="fas fa-heart"></i> Yêu thích';
                    }
                }
            })
            .catch(error => {
                console.error('Error updating count:', error);
            });
        }
        
        // Check if favorites list is empty
        function checkEmptyState() {
            const grid = document.querySelector('.tours-grid');
            if (grid && grid.children.length === 0) {
                const section = document.querySelector('.favorites-section .container');
                section.innerHTML = `
                    <div class="favorites-header">
                        <h1><i class="fas fa-heart"></i> Tours Yêu Thích</h1>
                        <p>Những tour du lịch bạn đã lưu lại để khám phá sau này</p>
                    </div>
                    <div class="empty-favorites">
                        <i class="far fa-heart"></i>
                        <h2>Chưa có tour yêu thích</h2>
                        <p>Hãy khám phá và lưu lại những tour du lịch hấp dẫn nhất!</p>
                        <a href="${pageContext.request.contextPath}/TourListServlet" class="btn-primary">
                            <i class="fas fa-compass"></i> Khám phá tours
                        </a>
                    </div>
                `;
            }
        }
        
        // Show toast notification
        function showToast(message, type) {
            const toast = document.getElementById('toast');
            const toastMessage = document.getElementById('toast-message');
            
            toast.className = 'toast ' + type;
            toastMessage.textContent = message;
            
            if (type === 'success') {
                toast.querySelector('i').className = 'fas fa-check-circle';
            } else {
                toast.querySelector('i').className = 'fas fa-exclamation-circle';
            }
            
            toast.classList.add('show');
            
            setTimeout(() => {
                toast.classList.remove('show');
            }, 3000);
        }
        
        // Close dialog when clicking outside
        document.getElementById('confirm-remove').addEventListener('click', function(e) {
            if (e.target === this) {
                closeConfirmDialog();
            }
        });
        
        // ESC key to close dialog
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeConfirmDialog();
            }
        });
    </script>
</body>
</html>
