package Controller;

import Model.Booking;
import Model.User;
import DAO.BookingDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing bookings (Admin)
 */
@WebServlet("/Admin/BookingServlet")
public class BookingAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;
    
    public BookingAdminServlet() {
        super();
        bookingDAO = new BookingDAO();
    }
    
    /**
     * Check if user is admin
     */
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }
    
    /**
     * Require admin authentication
     */
    private void requireAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
    }
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requireAdmin(request, response);
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listBookings(request, response);
                break;
            case "confirm":
                confirmBooking(request, response);
                break;
            case "cancel":
                cancelBooking(request, response);
                break;
            case "complete":
                completeBooking(request, response);
                break;
            case "delete":
                deleteBooking(request, response);
                break;
            case "view":
                viewBooking(request, response);
                break;
            default:
                listBookings(request, response);
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requireAdmin(request, response);
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "confirm":
                confirmBooking(request, response);
                break;
            case "cancel":
                cancelBooking(request, response);
                break;
            case "complete":
                completeBooking(request, response);
                break;
            case "delete":
                deleteBooking(request, response);
                break;
            default:
                listBookings(request, response);
        }
    }
    
    /**
     * List all bookings with filter and search
     * SỬA: Thêm xử lý filter và search
     */
    private void listBookings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số filter và search
            String statusFilter = request.getParameter("status");
            String searchTerm = request.getParameter("search");
            
            // Lưu lại giá trị filter để hiển thị trong form
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("searchTerm", searchTerm);
            
            List<Booking> bookings;
            
            // Xử lý tìm kiếm và lọc
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Tìm kiếm theo từ khóa
                bookings = bookingDAO.searchBookings(searchTerm.trim());
                
                // Nếu có cả search và filter
                if (statusFilter != null && !statusFilter.isEmpty()) {
                    bookings = bookingDAO.filterBookingsByStatusAndSearch(statusFilter, searchTerm.trim());
                }
            } else if (statusFilter != null && !statusFilter.isEmpty()) {
                // Chỉ lọc theo trạng thái
                bookings = bookingDAO.getBookingsByStatus(statusFilter);
            } else {
                // Lấy tất cả
                bookings = bookingDAO.getAllBookings();
            }
            
            int totalBookings = bookingDAO.getTotalCount();
            int pendingCount = bookingDAO.countByStatus("PENDING");
            int confirmedCount = bookingDAO.countByStatus("CONFIRMED");
            int cancelledCount = bookingDAO.countByStatus("CANCELLED");
            
            request.setAttribute("bookings", bookings);
            request.setAttribute("totalBookings", totalBookings);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("confirmedCount", confirmedCount);
            request.setAttribute("cancelledCount", cancelledCount);
            
            request.getRequestDispatcher("/WEB-INF/Admin/BookingList.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error listing bookings: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách đặt tour.");
            request.getRequestDispatcher("/WEB-INF/Admin/BookingList.jsp").forward(request, response);
        }
    }
    
    /**
     * View booking details
     */
    private void viewBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            Booking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                request.setAttribute("error", "Đặt tour không tồn tại.");
                listBookings(request, response);
                return;
            }
            
            request.setAttribute("booking", booking);
            request.getRequestDispatcher("/WEB-INF/Admin/BookingDetail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đặt tour không hợp lệ.");
            listBookings(request, response);
        }
    }
    
    /**
     * Confirm a booking
     */
    private void confirmBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            Booking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                request.setAttribute("error", "Đặt tour không tồn tại.");
                listBookings(request, response);
                return;
            }
            
            if (!booking.isPending()) {
                request.setAttribute("error", "Chỉ có thể xác nhận đặt tour đang chờ.");
                listBookings(request, response);
                return;
            }
            
            boolean success = bookingDAO.confirmBooking(bookingId);
            
            if (success) {
                request.setAttribute("success", "Xác nhận đặt tour thành công!");
            } else {
                request.setAttribute("error", "Xác nhận đặt tour thất bại.");
            }
            
            listBookings(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đặt tour không hợp lệ.");
            listBookings(request, response);
        }
    }
    
    /**
     * Cancel a booking
     */
    private void cancelBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            Booking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                request.setAttribute("error", "Đặt tour không tồn tại.");
                listBookings(request, response);
                return;
            }
            
            if (booking.isCancelled()) {
                request.setAttribute("error", "Đặt tour đã được hủy trước đó.");
                listBookings(request, response);
                return;
            }
            
            boolean success = bookingDAO.cancelBooking(bookingId);
            
            if (success) {
                request.setAttribute("success", "Hủy đặt tour thành công!");
            } else {
                request.setAttribute("error", "Hủy đặt tour thất bại.");
            }
            
            listBookings(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đặt tour không hợp lệ.");
            listBookings(request, response);
        }
    }
    
    /**
     * Mark a booking as completed
     */
    private void completeBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            Booking booking = bookingDAO.findById(bookingId);
            
            if (booking == null) {
                request.setAttribute("error", "Đặt tour không tồn tại.");
                listBookings(request, response);
                return;
            }
            
            if (!booking.isConfirmed()) {
                request.setAttribute("error", "Chỉ có thể hoàn thành đặt tour đã xác nhận.");
                listBookings(request, response);
                return;
            }
            
            boolean success = bookingDAO.completeBooking(bookingId);
            
            if (success) {
                request.setAttribute("success", "Đánh dấu hoàn thành thành công!");
            } else {
                request.setAttribute("error", "Cập nhật trạng thái thất bại.");
            }
            
            listBookings(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đặt tour không hợp lệ.");
            listBookings(request, response);
        }
    }
    
    /**
     * Delete a booking
     */
    private void deleteBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("id"));
            
            boolean success = bookingDAO.delete(bookingId);
            
            if (success) {
                request.setAttribute("success", "Xóa đặt tour thành công!");
            } else {
                request.setAttribute("error", "Xóa đặt tour thất bại.");
            }
            
            listBookings(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID đặt tour không hợp lệ.");
            listBookings(request, response);
        }
    }
}