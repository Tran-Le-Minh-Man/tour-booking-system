package Controller;

import Model.User;
import DAO.BookingDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for Revenue Reports (Admin)
 * Author: MiniMax Agent
 */
@WebServlet("/Admin/RevenueServlet")
public class RevenueServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingDAO bookingDAO;
    
    public RevenueServlet() {
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
        
        String period = request.getParameter("period");
        if (period == null || period.isEmpty()) {
            period = "month"; // Default: this month
        }
        
        // Calculate date range based on period
        Timestamp[] dateRange = getDateRange(period);
        Timestamp startDate = dateRange[0];
        Timestamp endDate = dateRange[1];
        
        // Get revenue data
        BigDecimal totalRevenue = bookingDAO.getTotalRevenue();
        BigDecimal periodRevenue = bookingDAO.getRevenueByDateRange(startDate, endDate);
        
        // Get booking statistics
        int totalBookings = bookingDAO.getTotalCount();
        int confirmedBookings = bookingDAO.countByStatus("CONFIRMED");
        int completedBookings = bookingDAO.countByStatus("COMPLETED");
        int pendingBookings = bookingDAO.countByStatus("PENDING");
        int cancelledBookings = bookingDAO.countByStatus("CANCELLED");
        
        // Get monthly revenue data (last 6 months)
        List<Map<String, Object>> monthlyRevenue = getMonthlyRevenueData();
        
        // Format dates for display
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        request.setAttribute("startDate", sdf.format(startDate));
        request.setAttribute("endDate", sdf.format(endDate));
        request.setAttribute("period", period);
        
        // Set data attributes
        request.setAttribute("totalRevenue", formatCurrency(totalRevenue));
        request.setAttribute("periodRevenue", formatCurrency(periodRevenue));
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("confirmedBookings", confirmedBookings);
        request.setAttribute("completedBookings", completedBookings);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("cancelledBookings", cancelledBookings);
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        
        request.getRequestDispatcher("/WEB-INF/Admin/Revenue.jsp").forward(request, response);
    }
    
    /**
     * Get date range based on period
     */
    private Timestamp[] getDateRange(String period) {
        Calendar cal = Calendar.getInstance();
        Timestamp endDate = new Timestamp(cal.getTimeInMillis());
        
        Timestamp startDate;
        
        switch (period) {
            case "today":
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = new Timestamp(cal.getTimeInMillis());
                break;
                
            case "week":
                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = new Timestamp(cal.getTimeInMillis());
                break;
                
            case "month":
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = new Timestamp(cal.getTimeInMillis());
                break;
                
            case "year":
                cal.set(Calendar.DAY_OF_YEAR, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = new Timestamp(cal.getTimeInMillis());
                break;
                
            default:
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                startDate = new Timestamp(cal.getTimeInMillis());
        }
        
        return new Timestamp[]{startDate, endDate};
    }
    
    /**
     * Get monthly revenue data for the last 6 months
     */
    private List<Map<String, Object>> getMonthlyRevenueData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM/yyyy");
        
        for (int i = 5; i >= 0; i--) {
            Calendar monthCal = (Calendar) cal.clone();
            monthCal.add(Calendar.MONTH, -i);
            
            monthCal.set(Calendar.DAY_OF_MONTH, 1);
            monthCal.set(Calendar.HOUR_OF_DAY, 0);
            monthCal.set(Calendar.MINUTE, 0);
            monthCal.set(Calendar.SECOND, 0);
            monthCal.set(Calendar.MILLISECOND, 0);
            Timestamp start = new Timestamp(monthCal.getTimeInMillis());
            
            monthCal.set(Calendar.DAY_OF_MONTH, monthCal.getActualMaximum(Calendar.DAY_OF_MONTH));
            monthCal.set(Calendar.HOUR_OF_DAY, 23);
            monthCal.set(Calendar.MINUTE, 59);
            monthCal.set(Calendar.SECOND, 59);
            monthCal.set(Calendar.MILLISECOND, 999);
            Timestamp end = new Timestamp(monthCal.getTimeInMillis());
            
            BigDecimal revenue = bookingDAO.getRevenueByDateRange(start, end);
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", monthFormat.format(monthCal.getTime()));
            monthData.put("revenue", revenue);
            monthData.put("formattedRevenue", formatCurrency(revenue));
            
            data.add(monthData);
        }
        
        return data;
    }
    
    /**
     * Format currency for display
     */
    private String formatCurrency(BigDecimal amount) {
        if (amount == null) return "0đ";
        return String.format("%,.0fđ", amount);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}