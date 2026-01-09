package Controller;

import Model.User;
import DAO.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Servlet for handling user login Author: MiniMax Agent
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private static final int REMEMBER_ME_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days in seconds

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		userDAO = new UserDAO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		// Check if user is already logged in
		if (session != null && session.getAttribute("user") != null) {
			response.sendRedirect(request.getContextPath() + "/HomePage.jsp");
			return;
		}

		// Generate CSRF token for form
		session = request.getSession(true);
		String csrfToken = java.util.UUID.randomUUID().toString();
		session.setAttribute("_csrf_token", csrfToken);

		request.setAttribute("_csrf_token", csrfToken);

		// Check for success message from registration
		String success = (String) session.getAttribute("success");
		if (success != null) {
			request.setAttribute("success", success);
			session.removeAttribute("success"); // Remove after displaying
		}

		String path = "WEB-INF/Client/Login.jsp";
		request.getRequestDispatcher(path).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpSession session = request.getSession(true);

		try {
			// Get and validate CSRF token
			String sessionCsrfToken = (String) session.getAttribute("_csrf_token");
			String requestCsrfToken = request.getParameter("_csrf_token");

			if (sessionCsrfToken == null || !sessionCsrfToken.equals(requestCsrfToken)) {
				request.setAttribute("error", "Yêu cầu không hợp lệ. Vui lòng thử lại.");
				request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
				return;
			}

			// Get form parameters
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String rememberMe = request.getParameter("remember");

			// Basic validation
			if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
				request.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu.");
				request.setAttribute("email", email);
				request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
				return;
			}

			// Verify login credentials
			User user = userDAO.verifyLogin(email.trim().toLowerCase(), password);

			if (user == null) {
				// Login failed - use generic error message to prevent user enumeration
				request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
				request.setAttribute("email", email);
				request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
				return;
			}

			// Login successful - create new session to prevent session fixation
			session.invalidate();
			session = request.getSession(true);

			// Store user in session
			session.setAttribute("user", user);

			// Set session timeout (30 minutes)
			session.setMaxInactiveInterval(30 * 60);

			// Regenerate CSRF token after login
			String newCsrfToken = java.util.UUID.randomUUID().toString();
			session.setAttribute("_csrf_token", newCsrfToken);

			// Handle remember me functionality
			if ("on".equals(rememberMe)) {
				String rememberToken = generateRememberToken();
				int userId = user.getUserId();

				// Store remember token in database (simplified - in production, store token
				// with expiry)
				// For now, we'll store it in a cookie with user info
				Cookie rememberCookie = new Cookie("remember_token", userId + ":" + rememberToken);
				rememberCookie.setMaxAge(REMEMBER_ME_COOKIE_MAX_AGE);
				rememberCookie.setPath("/");
				rememberCookie.setHttpOnly(true); // Prevent XSS attacks
				// In production, add: rememberCookie.setSecure(true); for HTTPS
				response.addCookie(rememberCookie);

				// Store token in session for validation
				session.setAttribute("remember_token", rememberToken);
			}

			// Redirect to home page
			response.sendRedirect(request.getContextPath() + "/HomePage.jsp");

		} catch (Exception e) {
			System.err.println("Login error: " + e.getMessage());
			request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
			request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
		}
	}

	/**
	 * Check for remember me cookie and auto-login
	 */
	private String checkRememberMeCookie(HttpServletRequest request, HttpSession session) {
		if (session == null) {
			return "Session error";
		}

		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if ("remember_token".equals(cookie.getName())) {
				String token = cookie.getValue();
				String storedToken = (String) session.getAttribute("remember_token");

				// Validate token
				if (token != null && storedToken != null && token.equals(storedToken)) {
					// Extract user ID from token
					String[] parts = token.split(":");
					if (parts.length >= 1) {
						try {
							int userId = Integer.parseInt(parts[0]);
							UserDAO userDAO = new UserDAO();
							User user = userDAO.findById(userId);

							if (user != null) {
								session.setAttribute("user", user);
								return null; // Auto-login successful
							}
						} catch (NumberFormatException e) {
							return "Invalid token";
						}
					}
				}
				return "Invalid or expired token";
			}
		}

		return null; // No remember me cookie found
	}

	/**
	 * Generate a secure remember token
	 */
	private String generateRememberToken() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
}