package Controller;

import Model.Tour;
import Model.User;
import DAO.TourDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * Servlet for managing tours (Admin)
 */
@WebServlet("/Admin/TourServlet")
public class TourAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TourDAO tourDAO;

	public TourAdminServlet() {
		super();
		tourDAO = new TourDAO();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * Check if user is admin
	 */
	private boolean isAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return false;

		User user = (User) session.getAttribute("user");
		return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
	}

	/**
	 * Forward to admin page if not admin
	 */
	private void requireAdmin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (!isAdmin(request)) {
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
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
			listTours(request, response);
			break;
		case "add":
			showAddForm(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteTour(request, response);
			break;
		case "view":
			viewTour(request, response);
			break;
		default:
			listTours(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		requireAdmin(request, response);

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "add":
			addTour(request, response);
			break;
		case "edit":
			updateTour(request, response);
			break;
		case "delete":
			deleteTour(request, response);
			break;
		default:
			listTours(request, response);
		}
	}

	/**
	 * List all tours
	 */
	private void listTours(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			 String searchTerm = request.getParameter("search");
	            
	            // Lưu lại giá trị search để hiển thị trong form
	            request.setAttribute("searchTerm", searchTerm);
	            
	            List<Tour> tours;
	            
	            // Xử lý tìm kiếm
	            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
	                tours = tourDAO.searchTours(searchTerm.trim());
	            } else {
	                tours = tourDAO.getAllTours();
	            }
			int totalTours = tourDAO.getTotalCount();
			int activeTours = tourDAO.countByStatus("ACTIVE");
			int inactiveTours = tourDAO.countByStatus("INACTIVE");

			request.setAttribute("tours", tours);
			request.setAttribute("totalTours", totalTours);
			request.setAttribute("activeTours", activeTours);
			request.setAttribute("inactiveTours", inactiveTours);

			request.getRequestDispatcher("/WEB-INF/Admin/TourList.jsp").forward(request, response);

		} catch (Exception e) {
			System.err.println("Error listing tours: " + e.getMessage());
			request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách tour.");
			request.getRequestDispatcher("/WEB-INF/Admin/TourList.jsp").forward(request, response);
		}
	}

	/**
	 * Show add tour form
	 */
	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("tour", null);
		request.setAttribute("action", "add");
		request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
	}

	/**
	 * Show edit tour form
	 */
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int tourId = Integer.parseInt(request.getParameter("id"));
			Tour tour = tourDAO.findById(tourId);

			if (tour == null) {
				request.setAttribute("error", "Tour không tồn tại.");
				listTours(request, response);
				return;
			}

			request.setAttribute("tour", tour);
			request.setAttribute("action", "edit");
			request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "ID tour không hợp lệ.");
			listTours(request, response);
		}
	}

	/**
	 * View tour details
	 */
	private void viewTour(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int tourId = Integer.parseInt(request.getParameter("id"));
			Tour tour = tourDAO.findById(tourId);

			if (tour == null) {
				request.setAttribute("error", "Tour không tồn tại.");
				listTours(request, response);
				return;
			}

			request.setAttribute("tour", tour);
			request.getRequestDispatcher("/WEB-INF/Admin/TourDetail.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "ID tour không hợp lệ.");
			listTours(request, response);
		}
	}

	/**
	 * Add new tour
	 */
	private void addTour(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Get form parameters
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String destination = request.getParameter("destination");
			String departureDateStr = request.getParameter("departureDate");
			String durationStr = request.getParameter("duration");
			String priceStr = request.getParameter("price");
			String maxParticipantsStr = request.getParameter("maxParticipants");
			String imageUrl = request.getParameter("imageUrl");
			String status = request.getParameter("status");

			// Validation
			StringBuilder errors = new StringBuilder();

			if (name == null || name.trim().isEmpty()) {
				errors.append("Tên tour không được để trống.<br>");
			}

			if (destination == null || destination.trim().isEmpty()) {
				errors.append("Điểm đến không được để trống.<br>");
			}

			int duration = 0;
			if (durationStr != null && !durationStr.trim().isEmpty()) {
				try {
					duration = Integer.parseInt(durationStr.trim());
					if (duration <= 0) {
						errors.append("Thời gian tour phải lớn hơn 0.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Thời gian tour không hợp lệ.<br>");
				}
			}

			BigDecimal price = null;
			if (priceStr != null && !priceStr.trim().isEmpty()) {
				try {
					price = new BigDecimal(priceStr.trim().replace(",", ""));
					if (price.compareTo(BigDecimal.ZERO) < 0) {
						errors.append("Giá tour không được âm.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Giá tour không hợp lệ.<br>");
				}
			}

			int maxParticipants = 0;
			if (maxParticipantsStr != null && !maxParticipantsStr.trim().isEmpty()) {
				try {
					maxParticipants = Integer.parseInt(maxParticipantsStr.trim());
					if (maxParticipants <= 0) {
						errors.append("Số người tham gia tối đa phải lớn hơn 0.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Số người tham gia tối đa không hợp lệ.<br>");
				}
			}

			if (errors.length() > 0) {
				request.setAttribute("error", errors.toString());
				request.setAttribute("tour", createTourFromRequest(request));
				request.setAttribute("action", "add");
				request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
				return;
			}

			// Create tour object
			Tour tour = new Tour();
			tour.setName(name.trim());
			tour.setDescription(description != null ? description.trim() : "");
			tour.setDestination(destination.trim());

			if (departureDateStr != null && !departureDateStr.isEmpty()) {
				tour.setDepartureDate(Timestamp.valueOf(departureDateStr + " 00:00:00"));
			}

			tour.setDuration(duration);
			tour.setPrice(price);
			tour.setMaxParticipants(maxParticipants);
			tour.setCurrentParticipants(0);
			tour.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
			tour.setStatus(status != null ? status : "ACTIVE");

			// Insert to database
			boolean success = tourDAO.insert(tour);

			if (success) {
				request.setAttribute("success", "Thêm tour thành công!");
				listTours(request, response);
			} else {
				request.setAttribute("error", "Thêm tour thất bại. Vui lòng thử lại.");
				request.setAttribute("tour", tour);
				request.setAttribute("action", "add");
				request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
			}

		} catch (Exception e) {
			System.err.println("Error adding tour: " + e.getMessage());
			request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			request.setAttribute("tour", createTourFromRequest(request));
			request.setAttribute("action", "add");
			request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
		}
	}

	/**
	 * Update existing tour
	 */
	private void updateTour(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String tourIdStr = request.getParameter("tourId");

			if (tourIdStr == null || tourIdStr.trim().isEmpty()) {
				request.setAttribute("error", "ID tour không hợp lệ.");
				listTours(request, response);
				return;
			}

			int tourId = Integer.parseInt(tourIdStr.trim());
			Tour existingTour = tourDAO.findById(tourId);

			if (existingTour == null) {
				request.setAttribute("error", "Tour không tồn tại.");
				listTours(request, response);
				return;
			}

			// Get form parameters
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String destination = request.getParameter("destination");
			String departureDateStr = request.getParameter("departureDate");
			String durationStr = request.getParameter("duration");
			String priceStr = request.getParameter("price");
			String maxParticipantsStr = request.getParameter("maxParticipants");
			String currentParticipantsStr = request.getParameter("currentParticipants");
			String imageUrl = request.getParameter("imageUrl");
			String status = request.getParameter("status");

			// Validation
			StringBuilder errors = new StringBuilder();

			if (name == null || name.trim().isEmpty()) {
				errors.append("Tên tour không được để trống.<br>");
			}

			if (destination == null || destination.trim().isEmpty()) {
				errors.append("Điểm đến không được để trống.<br>");
			}

			int duration = 0;
			if (durationStr != null && !durationStr.trim().isEmpty()) {
				try {
					duration = Integer.parseInt(durationStr.trim());
					if (duration <= 0) {
						errors.append("Thời gian tour phải lớn hơn 0.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Thời gian tour không hợp lệ.<br>");
				}
			}

			BigDecimal price = null;
			if (priceStr != null && !priceStr.trim().isEmpty()) {
				try {
					price = new BigDecimal(priceStr.trim().replace(",", ""));
					if (price.compareTo(BigDecimal.ZERO) < 0) {
						errors.append("Giá tour không được âm.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Giá tour không hợp lệ.<br>");
				}
			}

			int maxParticipants = 0;
			if (maxParticipantsStr != null && !maxParticipantsStr.trim().isEmpty()) {
				try {
					maxParticipants = Integer.parseInt(maxParticipantsStr.trim());
					if (maxParticipants <= 0) {
						errors.append("Số người tham gia tối đa phải lớn hơn 0.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Số người tham gia tối đa không hợp lệ.<br>");
				}
			}

			int currentParticipants = 0;
			if (currentParticipantsStr != null && !currentParticipantsStr.trim().isEmpty()) {
				try {
					currentParticipants = Integer.parseInt(currentParticipantsStr.trim());
					if (currentParticipants < 0) {
						errors.append("Số người hiện tại không được âm.<br>");
					}
				} catch (NumberFormatException e) {
					errors.append("Số người hiện tại không hợp lệ.<br>");
				}
			}

			if (errors.length() > 0) {
				request.setAttribute("error", errors.toString());
				request.setAttribute("tour", createTourFromRequest(request));
				request.setAttribute("action", "edit");
				request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
				return;
			}

			// Update tour object
			existingTour.setName(name.trim());
			existingTour.setDescription(description != null ? description.trim() : "");
			existingTour.setDestination(destination.trim());

			if (departureDateStr != null && !departureDateStr.isEmpty()) {
				existingTour.setDepartureDate(Timestamp.valueOf(departureDateStr + " 00:00:00"));
			} else {
				existingTour.setDepartureDate(null);
			}

			existingTour.setDuration(duration);
			existingTour.setPrice(price);
			existingTour.setMaxParticipants(maxParticipants);
			existingTour.setCurrentParticipants(currentParticipants);
			existingTour.setImageUrl(imageUrl != null ? imageUrl.trim() : "");
			existingTour.setStatus(status != null ? status : "ACTIVE");

			// Update in database
			boolean success = tourDAO.update(existingTour);

			if (success) {
				request.setAttribute("success", "Cập nhật tour thành công!");
				listTours(request, response);
			} else {
				request.setAttribute("error", "Cập nhật tour thất bại. Vui lòng thử lại.");
				request.setAttribute("tour", existingTour);
				request.setAttribute("action", "edit");
				request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
			}

		} catch (Exception e) {
			System.err.println("Error updating tour: " + e.getMessage());
			request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
			request.setAttribute("tour", createTourFromRequest(request));
			request.setAttribute("action", "edit");
			request.getRequestDispatcher("/WEB-INF/Admin/TourForm.jsp").forward(request, response);
		}
	}

	/**
	 * Delete tour
	 */
	private void deleteTour(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String tourIdStr = request.getParameter("id");

			if (tourIdStr == null || tourIdStr.trim().isEmpty()) {
				request.setAttribute("error", "ID tour không hợp lệ.");
				listTours(request, response);
				return;
			}

			int tourId = Integer.parseInt(tourIdStr.trim());

			// Check if tour exists
			Tour tour = tourDAO.findById(tourId);
			if (tour == null) {
				request.setAttribute("error", "Tour không tồn tại.");
				listTours(request, response);
				return;
			}

			// Delete from database
			boolean success = tourDAO.delete(tourId);

			if (success) {
				request.setAttribute("success", "Xóa tour thành công!");
			} else {
				request.setAttribute("error", "Xóa tour thất bại. Vui lòng thử lại.");
			}

			listTours(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "ID tour không hợp lệ.");
			listTours(request, response);
		}
	}

	/**
	 * Create Tour object from request parameters
	 */
	private Tour createTourFromRequest(HttpServletRequest request) {
		Tour tour = new Tour();

		String tourIdStr = request.getParameter("tourId");
		if (tourIdStr != null && !tourIdStr.isEmpty()) {
			try {
				tour.setTourId(Integer.parseInt(tourIdStr));
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		tour.setName(request.getParameter("name"));
		tour.setDescription(request.getParameter("description"));
		tour.setDestination(request.getParameter("destination"));

		String departureDateStr = request.getParameter("departureDate");
		if (departureDateStr != null && !departureDateStr.isEmpty()) {
			tour.setDepartureDate(Timestamp.valueOf(departureDateStr + " 00:00:00"));
		}

		String durationStr = request.getParameter("duration");
		if (durationStr != null && !durationStr.isEmpty()) {
			try {
				tour.setDuration(Integer.parseInt(durationStr));
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		String priceStr = request.getParameter("price");
		if (priceStr != null && !priceStr.isEmpty()) {
			try {
				tour.setPrice(new BigDecimal(priceStr.replace(",", "")));
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		String maxParticipantsStr = request.getParameter("maxParticipants");
		if (maxParticipantsStr != null && !maxParticipantsStr.isEmpty()) {
			try {
				tour.setMaxParticipants(Integer.parseInt(maxParticipantsStr));
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		String currentParticipantsStr = request.getParameter("currentParticipants");
		if (currentParticipantsStr != null && !currentParticipantsStr.isEmpty()) {
			try {
				tour.setCurrentParticipants(Integer.parseInt(currentParticipantsStr));
			} catch (NumberFormatException e) {
				// ignore
			}
		}

		tour.setImageUrl(request.getParameter("imageUrl"));
		tour.setStatus(request.getParameter("status"));

		return tour;
	}
}