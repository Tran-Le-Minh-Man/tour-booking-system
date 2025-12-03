<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Du Lịch Biển Việt Nam - Travel</title>
    
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700&family=Pacifico&display=swap" rel="stylesheet">
    
    <!-- Icon  -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Custom CSS -->
    <link href="CSS/HomePage.css" rel="stylesheet">
</head>
<body>

<!-- Navbar -->
<header class="navbar">
    <div class="container">
        <div class="nav-brand">
            <a href="index.jsp">
                <span class="logo-text">Việt Nam</span> Travel
            </a>
        </div>
        <nav class="nav-menu">
            <ul>
                <li><a href="index.jsp" class="active">Trang chủ</a></li>
                <li><a href="TourListServlet">Tất cả tour</a></li>
                <li><a href="login.jsp">Đăng nhập</a></li>
                <li><a href="register.jsp">Đăng ký</a></li>
            </ul>
        </nav>
        <div class="nav-toggle">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</header>

<!-- Hero Section -->
<section class="hero">
    <div class="hero-overlay"></div>
    <div class="container">
        <div class="hero-content">
            <h1>Khám Phá Biển Đảo Việt Nam</h1>
            <p class="lead">Hành trình tuyệt vời cùng nắng vàng, biển xanh và cát trắng</p>


            <form action="SearchTourServlet" method="get" class="search-form">
                <div class="input-group">
                    <input type="text" name="destination" placeholder="Đi đâu? (Phú Quốc, Nha Trang...)">
                    <input type="date" name="departure_date">
                    <button type="submit">
                        <i class="fas fa-search"></i> Tìm Tour
                    </button>
                </div>
            </form>
        </div>
    </div>
</section>

<!-- ================== PHẦN 1: TOUR NỔI BẬT ================== -->
<section class="featured-tours">
    <div class="container">
        <h2 class="section-title">Tour Nổi Bật</h2>
        <div class="tours-grid">
            <div class="tour-card">
                <div class="tour-image">
                    <img src="images/kiet/phuquoc.jpg" alt="Phú Quốc">
                </div>
                <div class="tour-info">
                    <h3>Phú Quốc 3N2Đ - Thiên Đường Biển</h3>
                    <p class="duration"><i class="fas fa-clock"></i> 3 ngày 2 đêm</p>
                    <p class="price">8.990.000đ</p>
                    <a href="TourDetailServlet?tourId=1" class="btn">Xem chi tiết</a>
                </div>
            </div>
            <div class="tour-card">
                <div class="tour-image">
                    <img src="images/kiet/nhatrang.jpg" alt="Nha Trang">
                </div>
                <div class="tour-info">
                    <h3>Nha Trang - Vinpearl Land</h3>
                    <p class="duration"><i class="far fa-calendar-alt"></i> Khởi hành: 20/11/2025</p>
                    <p class="price">6.500.000đ</p>
                    <a href="TourDetailServlet?tourId=2" class="btn">Xem chi tiết</a>
                </div>
            </div>
            <!-- Tour 3 -->
            <div class="tour-card">
                <div class="tour-image">
                    <img src="images/kiet/danang.jpg" alt="Đà Nẵng">
                </div>
                <div class="tour-info">
                    <h3>Đà Nẵng - Bà Nà Hills</h3>
                    <p class="duration"><i class="fas fa-users"></i> Còn 5 chỗ</p>
                    <p class="price">5.800.000đ</p>
                    <a href="TourDetailServlet?tourId=3" class="btn">Xem chi tiết</a>
                </div>
            </div>
        </div>
        <div class="center">
            <a href="TourListServlet" class="btn btn-large">Xem tất cả tour</a>
        </div>
    </div>
</section>


<!-- ================== PHẦN 2: KHUYẾN MÃI NỔI BẬT ================== -->
<section class="promo">
    <div class="container">
        <h2 class="section-title text-center">Khuyến Mãi <span class="highlight">Hot</span> Trong Tuần</h2>
        <div class="promo-grid">
            <div class="promo-card">
                <div class="badge">GIẢM 20%</div>
                <img src="images/kiet/nhatrang-promo.jpg" alt="Nha Trang">
                <div class="promo-info">
                    <h3>Nha Trang 4N3Đ - Vinpearl + Lặn biển</h3>
                    <p class="old-price">8.500.000đ</p>
                    <p class="new-price">6.800.000đ</p>
                    <p class="countdown"><i class="fas fa-clock"></i> Còn 2 ngày</p>
                    <a href="TourDetailServlet?tourId=2" class="btn">Đặt ngay</a>
                </div>
            </div>
            <div class="promo-card">
                <div class="badge">CÒN 3 CHỖ</div>
                <img src="images/kiet/phuquoc-promo.jpg" alt="Phú Quốc">
                <div class="promo-info">
                    <h3>Phú Quốc Bay Thẳng - Resort 5*</h3>
                    <p class="old-price">12.900.000đ</p>
                    <p class="new-price">11.900.000đ</p>
                    <p class="countdown text-danger"><i class="fas fa-exclamation-triangle"></i> Sắp hết chỗ!</p>
                    <a href="TourDetailServlet?tourId=1" class="btn">Đặt ngay</a>
                </div>
            </div>
        </div>
    </div>
</section>


<!-- ================== PHẦN 3: ĐÁNH GIÁ KHÁCH HÀNG ================== -->
<section class="testimonials">
    <div class="container">
        <h2 class="section-title text-center">Khách Hàng Nói Gì?</h2>
        <div class="testimonials-grid">
            <div class="testimonial-card">
                <div class="stars">
                    <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
                </div>
                <p class="quote">"Tour Phú Quốc quá tuyệt! Biển đẹp, ăn ngon, hướng dẫn viên vui tính. Sẽ đi tiếp!"</p>
                <div class="author">
                    <img src="images/kiet/customer1.jpg" alt="Nguyễn Lan">
                    <div>
                        <strong>Nguyễn Lan</strong><br>
                        <small>Đi tour tháng 10/2025</small>
                    </div>
                </div>
            </div>
            <div class="testimonial-card">
                <div class="stars">
                    <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
                </div>
                <p class="quote">"Dịch vụ chuyên nghiệp, xe đưa đón đúng giờ. Rất hài lòng!"</p>
                <div class="author">
                    <img src="images/kiet/customer2.jpg" alt="Trần Minh">
                    <div>
                        <strong>Trần Minh</strong><br>
                        <small>Đi tour Nha Trang</small>
                    </div>
                </div>
            </div>
            <div class="testimonial-card">
                <div class="stars">
                    <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="far fa-star"></i>
                </div>
                <p class="quote">"Ổn, nhưng nên cải thiện thời gian ăn trưa. Còn lại tốt!"</p>
                <div class="author">
                    <img src="images/kiet/customer3.jpg" alt="Lê Hương">
                    <div>
                        <strong>Lê Hương</strong><br>
                        <small>Đi tour Đà Nẵng</small>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- ================== PHẦN 4: TẠI SAO CHỌN CHÚNG TÔI ================== -->
<section class="why-choose py-5">
    <div class="container">
        <h2 class="section-title text-center">Tại Sao Chọn <span class="highlight">Việt Nam Travel</span>?</h2>
        <div class="features-grid">
            <div class="feature-item">
                <div class="icon">
                    <i class="fas fa-dollar-sign"></i>
                </div>
                <h3>Giá Tốt Nhất</h3>
                <p>Cam kết giá cạnh tranh, không phí ẩn. So sánh để thấy sự khác biệt!</p>
            </div>
            <div class="feature-item">
                <div class="icon">
                    <i class="fas fa-headset"></i>
                </div>
                <h3>Hỗ Trợ 24/7</h3>
                <p>Tư vấn miễn phí trước, trong và sau chuyến đi. Luôn sẵn sàng!</p>
            </div>
            <div class="feature-item">
                <div class="icon">
                    <i class="fas fa-user-tie"></i>
                </div>
                <h3>Hướng Dẫn Viên Chuyên Nghiệp</h3>
                <p>Đội ngũ HDV giàu kinh nghiệm, nhiệt tình, am hiểu địa phương.</p>
            </div>
            <div class="feature-item">
                <div class="icon">
                    <i class="fas fa-shield-alt"></i>
                </div>
                <h3>Đảm Bảo Hoàn Tiền</h3>
                <p>Hủy tour trước 48h → hoàn 100%. An tâm đặt chỗ!</p>
            </div>
        </div>
    </div>
</section>
<!-- JavaScript cho menu mobile -->
<script>
    document.querySelector('.nav-toggle').addEventListener('click', function() {
        document.querySelector('.nav-menu').classList.toggle('active');
    });
</script>
<!-- Footer (giữ nguyên) -->
<footer class="footer">
    <div class="container">
        <p>&copy; 2025 Việt Nam Travel - Website đặt tour du lịch nội địa</p>
        <p>Đồ án lập trình web - <strong>...</strong></p>
    </div>
</footer>
</body>
</html>