<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html; charset=UTF-8"%>

<c:set var="current" value="${pageContext.request.requestURI}" />

<header class="navbar">
	<div class="container">

		<div class="nav-brand">
			<a href="${pageContext.request.contextPath}/HomePage.jsp"> <span
				class="logo-text">Việt Nam</span> Travel
			</a>
		</div>

		<nav class="nav-menu">
			<ul>

				<li><a href="${pageContext.request.contextPath}/HomePage.jsp"
					class="${fn:endsWith(current, '/HomePage.jsp') ? 'active' : ''}">
						Trang chủ </a></li>

				<li><a
					href="${pageContext.request.contextPath}/TourListServlet"
					class="${fn:contains(current, 'TourListServlet') ? 'active' : ''}">
						Tất cả tour </a></li>

				<c:choose>

					<c:when test="${not empty sessionScope.user}">

						<!-- User logged in - Show favorites, cart, and settings dropdown -->
						<li><a
							href="${pageContext.request.contextPath}/FavoritesServlet?action=list"
							class="${fn:contains(current, 'favorites') ? 'active' : ''}">
								<i class="fas fa-heart"></i> Yêu thích
						</a></li>

						<li><a
							href="${pageContext.request.contextPath}/BookingServlet"
							class="${fn:contains(current, 'cart') ? 'active' : ''}"> <i
								class="fas fa-shopping-cart"></i> Giỏ hàng
						</a></li>

						<!-- Settings Dropdown -->
						<li class="dropdown"><a href="#" class="dropdown-toggle">
								<i class="fas fa-user-cog"></i> Cài đặt <i
								class="fas fa-chevron-down"></i>
						</a>
							<div class="dropdown-menu">
								<a href="${pageContext.request.contextPath}/Edit_Profile"> <i
									class="fas fa-user-edit"></i> Chỉnh sửa thông tin
								</a> <a href="${pageContext.request.contextPath}/LogoutServlet">
									<i class="fas fa-sign-out-alt"></i> Đăng xuất
								</a>
								<c:if test="${sessionScope.user.role == 'ADMIN'}">
									<a
										href="${pageContext.request.contextPath}/Admin/Dashboard">
										<i class="fas fa-cogs"></i> Trang quản trị
									</a>
								</c:if>
							</div></li>

						<li class="user-greeting"><span>Xin chào,<strong
								class="user_name">${sessionScope.user.fullName}</strong></span></li>

					</c:when>

					<c:otherwise>

						<!-- Guest - Show login and register -->
						<li><a href="${pageContext.request.contextPath}/LoginServlet"
							class="${fn:contains(current, 'login') ? 'active' : ''}">
								Đăng nhập </a></li>

						<li><a
							href="${pageContext.request.contextPath}/RegisterServlet"
							class="btn-nav-register"> Đăng ký </a></li>

					</c:otherwise>

				</c:choose>

			</ul>
		</nav>

		<div class="nav-toggle">
			<span></span> <span></span> <span></span>
		</div>

	</div>
</header>

<style>
/* Dropdown Menu Styles */
.dropdown {
	position: relative;
}

.dropdown-toggle {
	display: flex;
	align-items: center;
	gap: 5px;
	cursor: pointer;
}

.dropdown-toggle i:last-child {
	font-size: 0.7rem;
	margin-left: 3px;
}

.dropdown-menu {
	position: absolute;
	top: 100%;
	right: 0;
	min-width: 200px;
	background: white;
	border-radius: 8px;
	box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
	opacity: 0;
	visibility: hidden;
	transform: translateY(-10px);
	transition: all 0.3s ease;
	z-index: 1000;
	margin-top: 5px;
}

.dropdown-menu::before {
	content: '';
	position: absolute;
	top: -8px;
	right: 20px;
	border-width: 0 8px 8px 8px;
	border-style: solid;
	border-color: transparent transparent white transparent;
}

.dropdown:hover .dropdown-menu, .dropdown:focus-within .dropdown-menu {
	opacity: 1;
	visibility: visible;
	transform: translateY(0);
}

.dropdown-menu a {
	display: flex;
	align-items: center;
	gap: 10px;
	padding: 12px 20px;
	color: #444;
	text-decoration: none;
	font-size: 0.95rem;
	transition: all 0.2s ease;
	border-radius: 0;
}

.dropdown-menu a:first-child {
	border-radius: 8px 8px 0 0;
}

.dropdown-menu a:last-child {
	border-radius: 0 0 8px 8px;
}

.dropdown-menu a:hover {
	background: #f0f7ff;
	color: #0C67B3;
}

.dropdown-menu a i {
	width: 18px;
	text-align: center;
	color: #0C67B3;
}

/* User greeting style */
.user-greeting .user_name {
	color: #FFFFFF;
	font-size: 0.9rem;
	padding: 0 15px;
}

.user-greeting strong {
	color: #0C67B3;
}

/* Navigation link styles */
.nav-menu ul li a.btn-nav-register {
	background: #0C67B3;
	color: white !important;
	padding: 8px 20px;
	border-radius: 20px;
	font-weight: 600;
	transition: all 0.3s ease;
}

.nav-menu ul li a.btn-nav-register:hover {
	background: #095195;
	transform: translateY(-2px);
}

/* Heart icon animation for favorites */
.nav-menu ul li a i.fa-heart {
	color: #e74c3c;
}

/* Cart icon */
.nav-menu ul li a i.fa-shopping-cart {
	color: #0C67B3;
}

/* Settings icon */
.nav-menu ul li a i.fa-user-cog {
	color: #666;
}

/* Responsive adjustments */
@media ( max-width : 992px) {
	.dropdown-menu {
		position: static;
		box-shadow: none;
		opacity: 1;
		visibility: visible;
		transform: none;
		display: none;
		background: #f8f9fa;
		border-radius: 8px;
		margin-top: 0;
	}
	.dropdown:hover .dropdown-menu, .dropdown:focus-within .dropdown-menu {
		display: block;
	}
	.dropdown-menu::before {
		display: none;
	}
	.user-greeting {
		display: none;
	}
}

@media ( max-width : 480px) {
	.nav-menu ul li a {
		padding: 10px 15px;
	}
	.dropdown-menu {
		min-width: 180px;
	}
}
</style>
