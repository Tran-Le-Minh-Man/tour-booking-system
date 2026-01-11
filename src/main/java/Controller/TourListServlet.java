package Controller;

import java.io.IOException;
import java.util.List;

import DAO.TourDAO;
import Model.Tour;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet for displaying and filtering tour list
 */
@WebServlet(name = "TourListServlet", urlPatterns = {"/TourListServlet"})
public class TourListServlet extends HttpServlet {

    private TourDAO tourDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        tourDAO = new TourDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get search and filter parameters
        String searchKeyword = request.getParameter("keyword");
        String destination = request.getParameter("destination");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        String duration = request.getParameter("duration");
        String sortBy = request.getParameter("sortBy");

		List<Tour> tours;

        // If there's a search keyword, search by keyword
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            tours = tourDAO.searchTours(searchKeyword.trim());
        } 
        // If there are filters applied
        else if (destination != null || minPrice != null || maxPrice != null || duration != null) {
            tours = tourDAO.getFilteredTours(destination, minPrice, maxPrice, duration);
        }
        // Otherwise get all active tours
        else {
            tours = tourDAO.getActiveTours();
        }

        // Apply sorting if specified
        if (sortBy != null && !sortBy.isEmpty()) {
            tours = sortTours(tours, sortBy);
        }

        // Get unique destinations for filter dropdown
        List<String> destinations = tourDAO.getAllDestinations();

        // Set attributes for JSP
        request.setAttribute("tours", tours);
        request.setAttribute("destinations", destinations);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("selectedDestination", destination);
        request.setAttribute("selectedMinPrice", minPrice);
        request.setAttribute("selectedMaxPrice", maxPrice);
        request.setAttribute("selectedDuration", duration);
        request.setAttribute("selectedSortBy", sortBy);

        // Forward to JSP page
        request.getRequestDispatcher("tour-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Sort tours based on the specified criteria
     */
    private List<Tour> sortTours(List<Tour> tours, String sortBy) {
        switch (sortBy) {
            case "price_asc":
                tours.sort((t1, t2) -> {
                    if (t1.getPrice() == null) return 1;
                    if (t2.getPrice() == null) return -1;
                    return t1.getPrice().compareTo(t2.getPrice());
                });
                break;
            case "price_desc":
                tours.sort((t1, t2) -> {
                    if (t1.getPrice() == null) return 1;
                    if (t2.getPrice() == null) return -1;
                    return t2.getPrice().compareTo(t1.getPrice());
                });
                break;
            case "date_asc":
                tours.sort((t1, t2) -> {
                    if (t1.getDepartureDate() == null) return 1;
                    if (t2.getDepartureDate() == null) return -1;
                    return t1.getDepartureDate().compareTo(t2.getDepartureDate());
                });
                break;
            case "date_desc":
                tours.sort((t1, t2) -> {
                    if (t1.getDepartureDate() == null) return 1;
                    if (t2.getDepartureDate() == null) return -1;
                    return t2.getDepartureDate().compareTo(t1.getDepartureDate());
                });
                break;
            case "duration_asc":
                tours.sort((t1, t2) -> Integer.compare(t1.getDuration(), t2.getDuration()));
                break;
            case "duration_desc":
                tours.sort((t1, t2) -> Integer.compare(t2.getDuration(), t1.getDuration()));
                break;
            default:
                // Default sorting by ID descending (newest first)
                break;
        }
        return tours;
    }
}
