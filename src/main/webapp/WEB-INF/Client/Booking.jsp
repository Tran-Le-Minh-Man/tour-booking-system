<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, Model.Booking, Model.Tour"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Đặt Tour của tôi - Vietnam Travel</title>

<!-- Google Fonts -->
<link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&family=Pacifico&display=swap" rel="stylesheet">

<!-- Icon -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

<!-- Custom CSS -->
<link href="${pageContext.request.contextPath}/CSS/HomePage.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/tour_list.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/CSS/Booking.css" rel="stylesheet">
</head>
<body>
    <!-- Import Header -->
    <jsp:include page="/WEB-INF/Common/header.jsp" />

    <!-- Breadcrumb -->
    <section class="breadcrumb-section">
        <div class="container">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/HomePage.jsp"><i class="fas fa-home"></i> Trang chủ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Đặt Tour của tôi</li>
                </ol>
            </nav>
        </div>
    </section>

    <%
    // Get data from request
    List<Booking> pendingBookings = (List<Booking>) request.getAttribute("pendingBookings");
    List<Booking> confirmedBookings = (List<Booking>) request.getAttribute("confirmedBookings");
    int totalBookings = 0;
    if (request.getAttribute("totalBookings") != null) {
        totalBookings = (Integer) request.getAttribute("totalBookings");
    }
    int pendingCount = (pendingBookings != null) ? pendingBookings.size() : 0;
    int confirmedCount = (confirmedBookings != null) ? confirmedBookings.size() : 0;
    %>

    <!-- Booking Page -->
    <section class="booking-page">
        <div class="container">
            
            <!-- Header -->
            <div class="booking-header">
                <h1><i class="fas fa-ticket-alt"></i> Đặt Tour của tôi</h1>
                <p>Quản lý các tour bạn đã đặt</p>
                <div class="booking-stats">
                    <div class="stat-item">
                        <i class="fas fa-list"></i> Tổng: <span class="count"><%= totalBookings %></span>
                    </div>
                    <div class="stat-item">
                        <i class="fas fa-clock"></i> Chờ: <span class="count"><%= pendingCount %></span>
                    </div>
                    <div class="stat-item">
                        <i class="fas fa-check"></i> Đã xác nhận: <span class="count"><%= confirmedCount %></span>
                    </div>
                </div>
            </div>
            
            <!-- Toast Container -->
            <div id="toast" class="toast">
                <i class="fas fa-check-circle"></i>
                <span id="toast-message"></span>
            </div>
            
            <!-- Confirm Cancel Dialog -->
            <div id="confirm-cancel" class="confirm-dialog-overlay">
                <div class="confirm-dialog">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h3>Hủy đặt tour?</h3>
                    <p>Bạn có chắc chắn muốn hủy đặt tour này không?</p>
                    <div class="btn-group">
                        <button class="btn btn-cancel-dialog" onclick="closeCancelDialog()">Không, giữ lại</button>
                        <button class="btn btn-confirm-cancel" id="confirm-cancel-btn">Có, hủy bỏ</button>
                    </div>
                </div>
            </div>
            
            <!-- Error/Success Messages -->
            <% if (request.getAttribute("error") != null) { %>
            <div style="background: #e74c3c; color: white; padding: 15px 20px; border-radius: 8px; margin-bottom: 20px;">
                <i class="fas fa-exclamation-circle"></i> <%= request.getAttribute("error") %>
            </div>
            <% } %>
            
            <% if (request.getAttribute("success") != null) { %>
            <div style="background: #27ae60; color: white; padding: 15px 20px; border-radius: 8px; margin-bottom: 20px;">
                <i class="fas fa-check-circle"></i> <%= request.getAttribute("success") %>
            </div>
            <% } %>
            
            <!-- Pending Bookings Section -->
            <div class="booking-section">
                <h2 class="section-title pending">
                    <i class="fas fa-clock"></i> Chờ xác nhận
                </h2>
                
                <% if (pendingBookings != null && !pendingBookings.isEmpty()) { %>
                <div class="bookings-list">
                    <% for (Booking booking : pendingBookings) { %>
                    <div class="booking-card pending">
                        <div class="booking-content">
                            <div class="booking-image">
                                <% if (booking.getTourImage() != null && !booking.getTourImage().isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/<%= booking.getTourImage() %>" 
                                     alt="<%= booking.getTourName() %>"
                                     onerror="this.onerror=null;this.src='https://via.placeholder.com/250x180?text=<%= booking.getTourDestination().replace(" ", "+") %>'">
                                <% } else { %>
                                <img src="https://via.placeholder.com/250x180?text=<%= booking.getTourDestination().replace(" ", "+") %>" 
                                     alt="<%= booking.getTourName() %>">
                                <% } %>
                            </div>
                            <div class="booking-info">
                                <h3 class="booking-tour-name">
                                    <a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=<%= booking.getTourId() %>"><%= booking.getTourName() %></a>
                                </h3>
                                
                                <div class="booking-meta">
                                    <div class="meta-item">
                                        <i class="fas fa-map-marker-alt"></i> <%= booking.getTourDestination() %>
                                    </div>
                                    <div class="meta-item">
                                        <i class="far fa-calendar-alt"></i> <%= booking.getTourDeparture() %>
                                    </div>
                                    <div class="meta-item">
                                        <i class="fas fa-clock"></i> <%= booking.getTourDuration() %> ngày
                                    </div>
                                </div>
                                
                                <div class="booking-details">
                                    <div class="detail-row">
                                        <span class="label">Số người:</span>
                                        <span class="value"><%= booking.getNumParticipants() %> người</span>
                                    </div>
                                    <div class="detail-row">
                                        <span class="label">Giá tour:</span>
                                        <span class="value"><%= String.format("%,d", booking.getTourPrice().longValue()) %>đ/người</span>
                                    </div>
                                    <div class="detail-row">
                                        <span class="label">Tổng tiền:</span>
                                        <span class="value" style="color: #0C67B3; font-weight: 700;"><%= booking.getFormattedPrice() %></span>
                                    </div>
                                    <div class="detail-row">
                                        <span class="label">Ngày đặt:</span>
                                        <span class="value"><%= booking.getFormattedDate() %></span>
                                    </div>
                                </div>
                                
                                <span class="booking-status status-pending"><%= booking.getFormattedStatus() %></span>
                                
                                <div class="booking-actions">
                                    <a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=<%= booking.getTourId() %>" class="btn-booking btn-view">
                                        <i class="fas fa-info-circle"></i> Chi tiết
                                    </a>
                                    <button class="btn-booking btn-cancel" onclick="showCancelDialog(<%= booking.getBookingId() %>)">
                                        <i class="fas fa-times"></i> Hủy đặt
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
                <% } else { %>
                <div class="empty-bookings">
                    <i class="far fa-clock"></i>
                    <h3>Không có đơn chờ xác nhận</h3>
                    <p>Các đơn đặt tour của bạn sẽ hiện thị ở đây</p>
                </div>
                <% } %>
            </div>
            
            <!-- Confirmed Bookings Section -->
            <div class="booking-section">
                <h2 class="section-title confirmed">
                    <i class="fas fa-check-circle"></i> Đã xác nhận
                </h2>
                
                <% if (confirmedBookings != null && !confirmedBookings.isEmpty()) { %>
                <div class="bookings-list">
                    <% for (Booking booking : confirmedBookings) { %>
                    <div class="booking-card confirmed">
                        <div class="booking-content">
                            <div class="booking-image">
                                <% if (booking.getTourImage() != null && !booking.getTourImage().isEmpty()) { %>
                                <img src="${pageContext.request.contextPath}/<%= booking.getTourImage() %>" 
                                     alt="<%= booking.getTourName() %>"
                                     onerror="this.onerror=null;this.src='https://via.placeholder.com/250x180?text=<%= booking.getTourDestination().replace(" ", "+") %>'">
                                <% } else { %>
                                <img src="https://via.placeholder.com/250x180?text=<%= booking.getTourDestination().replace(" ", "+") %>" 
                                     alt="<%= booking.getTourName() %>">
                                <% } %>
                            </div>
                            <div class="booking-info">
                                <h3 class="booking-tour-name">
                                    <a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=<%= booking.getTourId() %>"><%= booking.getTourName() %></a>
                                </h3>
                                
                                <div class="booking-meta">
                                    <div class="meta-item">
                                        <i class="fas fa-map-marker-alt"></i> <%= booking.getTourDestination() %>
                                    </div>
                                    <div class="meta-item">
                                        <i class="far fa-calendar-alt"></i> <%= booking.getTourDeparture() %>
                                    </div>
                                    <div class="meta-item">
                                        <i class="fas fa-clock"></i> <%= booking.getTourDuration() %> ngày
                                    </div>
                                </div>
                                
                                <div class="booking-details">
                                    <div class="detail-row">
                                        <span class="label">Số người:</span>
                                        <span class="value"><%= booking.getNumParticipants() %> người</span>
                                    </div>
                                    <div class="detail-row">
                                        <span class="label">Tổng tiền:</span>
                                        <span class="value" style="color: #27ae60; font-weight: 700;"><%= booking.getFormattedPrice() %></span>
                                    </div>
                                    <div class="detail-row">
                                        <span class="label">Ngày đặt:</span>
                                        <span class="value"><%= booking.getFormattedDate() %></span>
                                    </div>
                                </div>
                                
                                <span class="booking-status status-confirmed"><%= booking.getFormattedStatus() %></span>
                                
                                <div class="booking-actions">
                                    <a href="${pageContext.request.contextPath}/TourDetailServlet?tourId=<%= booking.getTourId() %>" class="btn-booking btn-view">
                                        <i class="fas fa-info-circle"></i> Chi tiết
                                    </a>
                                    <a href="${pageContext.request.contextPath}/BookingServlet?action=complete&bookingId=<%= booking.getBookingId() %>" class="btn-booking btn-complete">
                                        <i class="fas fa-check"></i> Hoàn tất
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <% } %>
                </div>
                <% } else { %>
                <div class="empty-bookings">
                    <i class="far fa-check-circle"></i>
                    <h3>Chưa có tour đã xác nhận</h3>
                    <p>Các tour đã được xác nhận sẽ hiện thị ở đây</p>
                </div>
                <% } %>
            </div>
            
        </div>
    </section>

    <!-- Import Footer -->
    <jsp:include page="/WEB-INF/Common/footer.jsp" />

    <!-- JavaScript -->
    <script>
        let bookingToCancel = null;
        
        function showCancelDialog(bookingId) {
            bookingToCancel = bookingId;
            document.getElementById('confirm-cancel').classList.add('show');
        }
        
        function closeCancelDialog() {
            bookingToCancel = null;
            document.getElementById('confirm-cancel').classList.remove('show');
        }
        
        document.getElementById('confirm-cancel-btn').addEventListener('click', function() {
            if (bookingToCancel) {
                window.location.href = '${pageContext.request.contextPath}/BookingServlet?action=cancel&bookingId=' + bookingToCancel;
            }
            closeCancelDialog();
        });
        
        // Close dialog when clicking outside
        document.getElementById('confirm-cancel').addEventListener('click', function(e) {
            if (e.target === this) {
                closeCancelDialog();
            }
        });
        
        // Close dialog on Escape key
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeCancelDialog();
            }
        });
        
        // Auto-hide messages after 5 seconds
        setTimeout(function() {
            const messages = document.querySelectorAll('[style*="background: #e74c3c"], [style*="background: #27ae60"]');
            messages.forEach(function(msg) {
                msg.style.display = 'none';
            });
        }, 5000);
    </script>
</body>
</html>
