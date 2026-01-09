<!-- Tran Le Minh Man - 23130186-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Tìm kiếm Tour</title>

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Pacifico&display=swap"
	rel="stylesheet">

<!-- Icon  -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link href="CSS/HomePage.css" rel="stylesheet">
</head>

<body>
	<!-- Navbar -->
	<header class="navbar">
		<div class="container">
			<div class="nav-brand">
				<a href="index.jsp"> <span class="logo-text">Việt Nam</span>
					Travel
				</a>
			</div>
			<nav class="nav-menu">
				<ul>
					<li><a href="index.jsp">Trang chủ</a></li>
					<li><a href="TourListServlet" class="active">Tất cả tour</a></li>
					<li><a href="${pageContext.request.contextPath}/LoginServlet">Đăng
							nhập</a></li>
					<li><a
						href="${pageContext.request.contextPath}/RegisterServlet">Đăng
							ký</a></li>
				</ul>
			</nav>
			<div class="nav-toggle">
				<span></span> <span></span> <span></span>
			</div>
		</div>
	</header>
</body>
</html>