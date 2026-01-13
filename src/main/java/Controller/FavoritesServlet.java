package Controller;

import DAO.FavoritesDAO;
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
 * Servlet for handling user favorites (wishlist)
 */
@WebServlet(name = "FavoritesServlet", urlPatterns = {"/FavoritesServlet", "/favorites"})
public class FavoritesServlet extends HttpServlet {

    private FavoritesDAO favoritesDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        favoritesDAO = new FavoritesDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        // Handle different actions
        if ("list".equals(action)) {
            // Get user's favorite tours
            List<Tour> favorites = favoritesDAO.getUserFavorites(user.getUserId());
            request.setAttribute("favorites", favorites);
            request.getRequestDispatcher("WEB-INF/Client/Favorites.jsp").forward(request, response);
        } else if ("count".equals(action)) {
            // Get favorite count (for header badge)
            int count = favoritesDAO.getFavoriteCount(user.getUserId());
            response.setContentType("application/json");
            response.getWriter().write("{\"count\":" + count + "}");
        } else {
            // Default: show favorites list
            List<Tour> favorites = favoritesDAO.getUserFavorites(user.getUserId());
            request.setAttribute("favorites", favorites);
            request.getRequestDispatcher("WEB-INF/Client/Favorites.jsp").forward(request, response);
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
                request.getContextPath() + "/LoginServlet?redirect=" + request.getParameter("redirectUrl") + "\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        String tourIdStr = request.getParameter("tourId");
        
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
        
        try {
            System.out.println("=== FavoritesServlet ===");
            System.out.println("Action: " + action);
            System.out.println("TourId: " + tourId);
            System.out.println("User: " + (user != null ? user.getEmail() : "NULL"));
            
            if ("toggle".equals(action)) {
                // Toggle favorite status
                boolean isNowFavorite = favoritesDAO.toggleFavorite(user.getUserId(), tourId);
                int newCount = favoritesDAO.getFavoriteCount(user.getUserId());
                
                System.out.println("Toggle result - isFavorite: " + isNowFavorite + ", count: " + newCount);
                
                response.getWriter().write("{\"status\":\"success\"," +
                    "\"isFavorite\":" + isNowFavorite + "," +
                    "\"message\":\"" + (isNowFavorite ? "Đã thêm vào yêu thích!" : "Đã xóa khỏi yêu thích!") + "\"," +
                    "\"count\":" + newCount + "}");
                
            } else if ("add".equals(action)) {
                // Add to favorites
                boolean added = favoritesDAO.addFavorite(user.getUserId(), tourId);
                int newCount = favoritesDAO.getFavoriteCount(user.getUserId());
                
                System.out.println("Add result - added: " + added + ", count: " + newCount);
                
                response.getWriter().write("{\"status\":\"success\"," +
                    "\"added\":" + added + "," +
                    "\"message\":\"Đã thêm vào yêu thích!\"," +
                    "\"count\":" + newCount + "}");
                
            } else if ("remove".equals(action)) {
                // Remove from favorites
                boolean removed = favoritesDAO.removeFavorite(user.getUserId(), tourId);
                int newCount = favoritesDAO.getFavoriteCount(user.getUserId());
                
                System.out.println("Remove result - removed: " + removed + ", count: " + newCount);
                
                response.getWriter().write("{\"status\":\"success\"," +
                    "\"removed\":" + removed + "," +
                    "\"message\":\"Đã xóa khỏi yêu thích!\"," +
                    "\"count\":" + newCount + "}");
                
            } else if ("check".equals(action)) {
                // Check if tour is favorited
                boolean isFav = favoritesDAO.isFavorite(user.getUserId(), tourId);
                response.getWriter().write("{\"status\":\"success\",\"isFavorite\":" + isFav + ",\"message\":\"OK\"}");
                
            } else {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Hành động không hợp lệ\"}");
            }
            
        } catch (Exception e) {
            System.err.println("FavoritesServlet Error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }
}
