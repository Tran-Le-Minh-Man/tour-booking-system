package Controller;

import DAO.FavoritesDAO;
import DAO.TourDAO;
import Model.Tour;
import Model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying tour details
 */
@WebServlet(name = "TourDetailServlet", urlPatterns = {"/TourDetailServlet", "/tour_detail"})
public class TourDetailServlet extends HttpServlet {

    private TourDAO tourDAO;
    private FavoritesDAO favoritesDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        tourDAO = new TourDAO();
        favoritesDAO = new FavoritesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get tourId parameter
        String tourIdStr = request.getParameter("tourId");
        
        // Validate tourId
        if (tourIdStr == null || tourIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/TourListServlet");
            return;
        }
        
        int tourId;
        try {
            tourId = Integer.parseInt(tourIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/TourListServlet");
            return;
        }
        
        // Fetch tour details
        Tour tour = tourDAO.findById(tourId);
        
        // If tour not found or not active, show errorr
        if (tour == null || !"ACTIVE".equalsIgnoreCase(tour.getStatus())) {
            request.setAttribute("errorMessage", "Tour không tồn tại hoặc đã ngừng hoạt động.");
            request.getRequestDispatcher("WEB-INF/Client/tour_detail.jsp").forward(request, response);
            return;
        }
        
        // Fetch related tours (same destination, excluding current tour)
        List<Tour> relatedTours = tourDAO.getRelatedTours(tour.getDestination(), tour.getTourId(), 4);
        
        // Check if user is logged in and get user object
        HttpSession session = request.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        User user = null;
        
        if (isLoggedIn) {
            user = (User) session.getAttribute("user");
        }
        
        // Check if tour is in user's favorites
        boolean isFavorite = false;
        if (isLoggedIn && user != null) {
            isFavorite = favoritesDAO.isFavorite(user.getUserId(), tourId);
        }
        
        // Set attributes for JSP
        request.setAttribute("tour", tour);
        request.setAttribute("relatedTours", relatedTours);
        request.setAttribute("isLoggedIn", isLoggedIn);
        request.setAttribute("isFavorite", isFavorite);
        
        // Forward to JSP page
        request.getRequestDispatcher("WEB-INF/Client/tour_detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
