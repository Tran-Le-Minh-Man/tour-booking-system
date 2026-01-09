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

						<li><a
							href="${pageContext.request.contextPath}/LogoutServlet"> Đăng
								xuất </a></li>

						<li>Xin chào, <strong>${sessionScope.user.fullName}</strong>
						</li>

					</c:when>

					<c:otherwise>

						<li><a href="${pageContext.request.contextPath}/LoginServlet"
							class="${fn:contains(current, 'login.jsp') ? 'active' : ''}">
								Đăng nhập </a></li>

						<li><a
							href="${pageContext.request.contextPath}/RegisterServlet"
							class="${fn:contains(current, 'register.jsp') ? 'active' : ''}">
								Đăng ký </a></li>

					</c:otherwise>

				</c:choose>

			</ul>
		</nav>

		<div class="nav-toggle">
			<span></span> <span></span> <span></span>
		</div>

	</div>
</header>