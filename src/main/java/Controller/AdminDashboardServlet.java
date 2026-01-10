package Controller;

import Model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for Admin Dashboard
 * Author: MiniMax Agent
 */
@WebServlet("/Admin/Dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public AdminDashboardServlet() {
        super();
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
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is admin
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        
        // Get statistics for dashboard
        try {
            DAO.BookingDAO bookingDAO = new DAO.BookingDAO();
            DAO.TourDAO tourDAO = new DAO.TourDAO();
            DAO.UserDAO userDAO = new DAO.UserDAO();
            
            request.setAttribute("totalTours", tourDAO.getTotalCount());
            request.setAttribute("totalBookings", bookingDAO.getTotalCount());
            request.setAttribute("pendingBookings", bookingDAO.countByStatus("PENDING"));
            request.setAttribute("totalUsers", userDAO.getTotalCount());
            request.setAttribute("recentBookings", bookingDAO.getRecentBookings(5));
        } catch (Exception e) {
            System.err.println("Error loading dashboard stats: " + e.getMessage());
            request.setAttribute("totalTours", 0);
            request.setAttribute("totalBookings", 0);
            request.setAttribute("pendingBookings", 0);
            request.setAttribute("totalUsers", 0);
        }
        
        request.getRequestDispatcher("/WEB-INF/Admin/AdminDashboard.jsp").forward(request, response);
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}