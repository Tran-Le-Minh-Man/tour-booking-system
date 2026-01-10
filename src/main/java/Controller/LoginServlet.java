package Controller;

import Model.User;
import DAO.UserDAO;
import Util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet for handling user login
 * Enhanced with improved security features
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    private static final int REMEMBER_ME_COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days in seconds
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final int LOCKOUT_TIME = 15 * 60; // 15 minutes in seconds
    
    // Rate limiting storage
    private static final ConcurrentHashMap<String, LoginAttempt> loginAttempts = new ConcurrentHashMap<>();
    
    /**
     * Inner class to track login attempts
     */
    private static class LoginAttempt {
        private int count;
        private long lastAttempt;
        
        public LoginAttempt() {
            this.count = 0;
            this.lastAttempt = System.currentTimeMillis();
        }
        
        public void increment() {
            this.count++;
            this.lastAttempt = System.currentTimeMillis();
        }
        
        public boolean isLocked() {
            if (count >= MAX_LOGIN_ATTEMPTS) {
                long lockoutEnd = lastAttempt + (LOCKOUT_TIME * 1000);
                return System.currentTimeMillis() < lockoutEnd;
            }
            return false;
        }
        
        public int getRemainingAttempts() {
            return Math.max(0, MAX_LOGIN_ATTEMPTS - count);
        }
    }
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        userDAO = new UserDAO();
    }
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize any servlet-level resources here
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if user is already logged in
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/HomePage.jsp");
            return;
        }

        // Check for remember me cookie and auto-login
        if (session == null) {
            session = request.getSession(true);
        }
        String rememberError = checkRememberMeCookie(request, response, session);
        if (rememberError == null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/HomePage.jsp");
            return;
        }

        // Generate CSRF token only if not exists
        String existingToken = (String) session.getAttribute("_csrf_token");
        if (existingToken == null) {
            String csrfToken = java.util.UUID.randomUUID().toString();
            session.setAttribute("_csrf_token", csrfToken);
            request.setAttribute("_csrf_token", csrfToken);
        } else {
            request.setAttribute("_csrf_token", existingToken);
        }

        // Check for success message from registration
        String success = (String) session.getAttribute("success");
        if (success != null) {
            request.setAttribute("success", success);
            session.removeAttribute("success");
        }

        // Check for lockout status
        String clientIP = getClientIP(request);
        LoginAttempt attempt = loginAttempts.get(clientIP);
        if (attempt != null && attempt.isLocked()) {
            long remainingTime = (attempt.lastAttempt + (LOCKOUT_TIME * 1000) - System.currentTimeMillis()) / 1000;
            request.setAttribute("error", "Tài khoản tạm khóa. Vui lòng thử lại sau " + remainingTime + " giây.");
        }

        String path = "WEB-INF/Client/Login.jsp";
        request.getRequestDispatcher(path).forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession(true);
        String clientIP = getClientIP(request);
        
        // Check for rate limiting
        LoginAttempt attempt = loginAttempts.get(clientIP);
        if (attempt == null) {
            attempt = new LoginAttempt();
            loginAttempts.put(clientIP, attempt);
        }
        
        if (attempt.isLocked()) {
            long remainingTime = (attempt.lastAttempt + (LOCKOUT_TIME * 1000) - System.currentTimeMillis()) / 1000;
            request.setAttribute("error", "Tài khoản tạm khóa. Vui lòng thử lại sau " + remainingTime + " giây.");
            request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
            return;
        }

        try {
            // Get and validate CSRF token
            String sessionCsrfToken = (String) session.getAttribute("_csrf_token");
            String requestCsrfToken = request.getParameter("_csrf_token");

            if (sessionCsrfToken == null || !sessionCsrfToken.equals(requestCsrfToken)) {
                attempt.increment();
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
                attempt.increment();
                request.setAttribute("error", "Vui lòng nhập đầy đủ email và mật khẩu.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
                return;
            }

            // Verify login credentials
            User user = userDAO.verifyLogin(email.trim().toLowerCase(), password);

            if (user == null) {
                // Login failed - increment attempt counter
                attempt.increment();
                request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
                request.setAttribute("email", email);
                request.getRequestDispatcher("WEB-INF/Client/Login.jsp").forward(request, response);
                return;
            }

            // Login successful - reset attempt counter
            loginAttempts.remove(clientIP);

            // Create new session to prevent session fixation
            session.invalidate();
            session = request.getSession(true);

            // Store user in session
            session.setAttribute("user", user);

            // Set session timeout (30 minutes)
            session.setMaxInactiveInterval(30 * 60);

            // Generate new CSRF token after login
            String newCsrfToken = java.util.UUID.randomUUID().toString();
            session.setAttribute("_csrf_token", newCsrfToken);

            // Handle remember me functionality with secure token storage
            if ("on".equals(rememberMe)) {
                String rememberToken = generateSecureToken();
                
                // Store token in database with expiry
                storeRememberToken(user.getUserId(), rememberToken);
                
                // Create secure cookie
                Cookie rememberCookie = new Cookie("remember_token", rememberToken);
                rememberCookie.setMaxAge(REMEMBER_ME_COOKIE_MAX_AGE);
                rememberCookie.setPath("/");
                rememberCookie.setHttpOnly(true);
                rememberCookie.setSecure(request.isSecure()); // Use HTTPS if available
                response.addCookie(rememberCookie);
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
     * Store remember token in database
     * Note: Tạm thời bỏ qua nếu cột không tồn tại trong database
     */
    private void storeRememberToken(int userId, String token) {
        // Tạm thời bỏ qua chức năng remember me do cột không tồn tại trong database
        System.err.println("Remember me feature temporarily disabled - columns not found in database");
        
        // Nếu muốn kích hoạt sau này, cần thêm 2 cột: remember_token (Text) và token_expiry (Date/Time)
        /*
        String sql = "UPDATE users SET remember_token = ?, token_expiry = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            long expiryTime = System.currentTimeMillis() + (REMEMBER_ME_COOKIE_MAX_AGE * 1000L);
            stmt.setString(1, token);
            stmt.setTimestamp(2, new java.sql.Timestamp(expiryTime));
            stmt.setInt(3, userId);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error storing remember token: " + e.getMessage());
        }
        */
    }

    /**
     * Check for remember me cookie and auto-login
     * Note: Tạm thời vô hiệu hóa do cột không tồn tại trong database
     */
    private String checkRememberMeCookie(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        // Tạm thời bỏ qua remember me - không kiểm tra cookie
        return null;
        
        /*
        if (session == null) return "Session error";

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if ("remember_token".equals(cookie.getName())) {
                String token = cookie.getValue();
                
                if (token == null || token.isEmpty()) {
                    return "Invalid token";
                }
                
                // Validate token against database
                User user = validateRememberToken(token);
                
                if (user != null) {
                    session.setAttribute("user", user);
                    // Update token for security
                    String newToken = generateSecureToken();
                    storeRememberToken(user.getUserId(), newToken);
                    
                    // Update cookie with new token
                    Cookie newCookie = new Cookie("remember_token", newToken);
                    newCookie.setMaxAge(REMEMBER_ME_COOKIE_MAX_AGE);
                    newCookie.setPath("/");
                    newCookie.setHttpOnly(true);
                    newCookie.setSecure(request.isSecure());
                    response.addCookie(newCookie);
                    
                    return null;
                }
                
                // Clear invalid token
                cookie.setValue("");
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                
                return "Invalid or expired token";
            }
        }

        return null;
        */
    }

    /**
     * Validate remember token against database
     * Note: Tạm thời vô hiệu hóa do cột không tồn tại trong database
     */
    private User validateRememberToken(String token) {
        // Tạm thời trả về null - không thực hiện auto-login
        return null;
        
        /*
        String sql = "SELECT id, email, password_hash, full_name, phone, role, created_at " +
                     "FROM users WHERE remember_token = ? AND token_expiry > ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, token);
            stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("id"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password_hash"));
                    user.setFullName(rs.getString("full_name"));
                    
                    String phone = rs.getString("phone");
                    user.setPhone(phone != null ? phone : "");
                    
                    user.setRole(rs.getString("role"));
                    
                    java.sql.Timestamp createdAt = rs.getTimestamp("created_at");
                    if (createdAt != null) {
                        user.setCreatedAt(createdAt.toString());
                    }
                    
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating remember token: " + e.getMessage());
        }
        
        return null;
        */
    }

    /**
     * Generate a secure remember token
     */
    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64]; // 512 bits
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Get client IP address
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}