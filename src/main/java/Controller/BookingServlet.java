package Controller;

import DAO.BookingDAO;
import DAO.TourDAO;
import Model.Booking;
import Model.Tour;
import Model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet for handling tour booking operations
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/BookingServlet", "/booking", "/cart"})
public class BookingServlet extends HttpServlet {

    private BookingDAO bookingDAO;
    private TourDAO tourDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        bookingDAO = new BookingDAO();
        tourDAO = new TourDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet?redirect=" + request.getRequestURL());
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        // Default action: show user's bookings/cart
        if (action == null || action.isEmpty()) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                showUserBookings(request, response, user);
                break;
            case "cancel":
                cancelBooking(request, response, user);
                break;
            default:
                showUserBookings(request, response, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng đăng nhập để tiếp tục\",\"redirect\":\"" + 
                request.getContextPath() + "/LoginServlet?redirect=" + request.getRequestURL() + "\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        String tourIdStr = request.getParameter("tourId");
        String quantityStr = request.getParameter("quantity");
        
        // Validate tourId
        if (tourIdStr == null || tourIdStr.trim().isEmpty()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Tour không hợp lệ\"}");
            return;
        }
        
        int tourId;
        try {
            tourId = Integer.parseInt(tourIdStr.trim());
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Tour không hợp lệ\"}");
            return;
        }
        
        // Validate quantity
        int quantity = 1;
        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityStr.trim());
                if (quantity < 1) quantity = 1;
            } catch (NumberFormatException e) {
                quantity = 1;
            }
        }
        
        try {
            System.out.println("=== BookingServlet ===");
            System.out.println("Action: " + action);
            System.out.println("TourId: " + tourId);
            System.out.println("Quantity: " + quantity);
            System.out.println("User: " + user.getEmail());
            
            if ("create".equals(action)) {
                createBooking(request, response, user, tourId, quantity);
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Hành động không hợp lệ\"}");
            }
            
        } catch (Exception e) {
            System.err.println("BookingServlet Error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Show user's bookings/cart page
     */
    private void showUserBookings(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        // Get all user bookings
        List<Booking> bookings = bookingDAO.getBookingsByUserId(user.getUserId());
        
        // Separate by status
        List<Booking> pendingBookings = new java.util.ArrayList<>();
        List<Booking> confirmedBookings = new java.util.ArrayList<>();
        
        for (Booking booking : bookings) {
            if ("PENDING".equalsIgnoreCase(booking.getStatus())) {
                pendingBookings.add(booking);
            } else if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                confirmedBookings.add(booking);
            }
        }
        
        request.setAttribute("bookings", bookings);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("confirmedBookings", confirmedBookings);
        request.setAttribute("totalBookings", bookings.size());
        
        request.getRequestDispatcher("WEB-INF/Client/Booking.jsp").forward(request, response);
    }
    
    /**
     * Create a new booking
     */
    private void createBooking(HttpServletRequest request, HttpServletResponse response, User user, int tourId, int quantity)
            throws ServletException, IOException {
        
        // Get tour details
        Tour tour = tourDAO.findById(tourId);
        
        if (tour == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Tour không tồn tại\"}");
            return;
        }
        
        if (!"ACTIVE".equalsIgnoreCase(tour.getStatus())) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Tour không còn hoạt động\"}");
            return;
        }
        
        if (quantity > tour.getAvailableSlots()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Số lượng không đủ. Chỉ còn " + tour.getAvailableSlots() + " chỗ\"}");
            return;
        }
        
        // Calculate total price
        BigDecimal totalPrice = tour.getPrice().multiply(BigDecimal.valueOf(quantity));
        
        // Create booking
        Booking booking = new Booking();
        booking.setUserId(user.getUserId());
        booking.setTourId(tourId);
        booking.setStatus("PENDING");
        booking.setNumParticipants(quantity);
        booking.setTotalPrice(totalPrice);
        booking.setNotes(request.getParameter("notes"));
        
        int bookingId = bookingDAO.createBooking(booking);
        
        if (bookingId > 0) {
            System.out.println("Booking created successfully with ID: " + bookingId);
            response.getWriter().write("{\"status\":\"success\"," +
                "\"message\":\"Đặt tour thành công!\", " +
                "\"bookingId\":" + bookingId + "}");
        } else {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Không thể tạo đơn đặt tour\"}");
        }
    }
    
    /**
     * Cancel a booking
     */
    private void cancelBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        String bookingIdStr = request.getParameter("bookingId");
        
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Booking ID không hợp lệ");
            showUserBookings(request, response, user);
            return;
        }
        
        int bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Booking ID không hợp lệ");
            showUserBookings(request, response, user);
            return;
        }
        
        // Verify booking belongs to user
        Booking booking = bookingDAO.findById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Đơn đặt tour không tồn tại");
            showUserBookings(request, response, user);
            return;
        }
        
        if (booking.getUserId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền hủy đơn này");
            showUserBookings(request, response, user);
            return;
        }
        
        // Only allow cancel PENDING bookings
        if (!"PENDING".equalsIgnoreCase(booking.getStatus())) {
            request.setAttribute("error", "Chỉ có thể hủy đơn đang chờ xác nhận");
            showUserBookings(request, response, user);
            return;
        }
        
        // Cancel the booking
        boolean success = bookingDAO.cancelBooking(bookingId);
        
        if (success) {
            request.setAttribute("success", "Hủy đặt tour thành công!");
        } else {
            request.setAttribute("error", "Không thể hủy đặt tour. Vui lòng thử lại.");
        }
        
        showUserBookings(request, response, user);
    }
    
    /**
     * Confirm booking (user can mark as completed after tour)
     */
    private void confirmBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        String bookingIdStr = request.getParameter("bookingId");
        
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Booking ID không hợp lệ");
            showUserBookings(request, response, user);
            return;
        }
        
        int bookingId;
        try {
            bookingId = Integer.parseInt(bookingIdStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Booking ID không hợp lệ");
            showUserBookings(request, response, user);
            return;
        }
        
        // Verify booking belongs to user
        Booking booking = bookingDAO.findById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Đơn đặt tour không tồn tại");
            showUserBookings(request, response, user);
            return;
        }
        
        if (booking.getUserId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền xác nhận đơn này");
            showUserBookings(request, response, user);
            return;
        }
        
        // Only CONFIRMED bookings can be marked as completed
        if (!"CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
            request.setAttribute("error", "Chỉ có thể hoàn tất đơn đã được xác nhận");
            showUserBookings(request, response, user);
            return;
        }
        
        // Mark as completed
        boolean success = bookingDAO.completeBooking(bookingId);
        
        if (success) {
            request.setAttribute("success", "Cập nhật trạng thái thành công!");
        } else {
            request.setAttribute("error", "Không thể cập nhật trạng thái. Vui lòng thử lại.");
        }
        
        showUserBookings(request, response, user);
    }
}
