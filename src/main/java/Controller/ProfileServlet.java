package Controller;

import DAO.UserDAO;
import Model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user profile editing and password changes
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/ProfileServlet", "/Edit_Profile", "/edit-profile"})
public class ProfileServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
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

        // Get the user from database to have latest data
        User latestUser = userDAO.findById(user.getUserId());
        if (latestUser != null) {
            request.setAttribute("user", latestUser);
        } else {
            request.setAttribute("user", user);
        }

        // Check which tab to show
        String tab = request.getParameter("tab");
        if (tab == null) {
            tab = "profile";
        }
        request.setAttribute("activeTab", tab);

        // Store the referring page for redirect after update
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.contains("/profile") && !referer.contains("/edit-profile") 
            && !referer.contains("/change-password")) {
            request.getSession().setAttribute("redirectAfterUpdate", referer);
        } else {
            request.getSession().setAttribute("redirectAfterUpdate", request.getContextPath() + "/HomePage.jsp");
        }

        request.getRequestDispatcher("/WEB-INF/Client/Edit_Profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Vui lòng đăng nhập\",\"redirect\":\"" +
                request.getContextPath() + "/LoginServlet\"}");
            return;
        }

        try {
            User currentUser = (User) session.getAttribute("user");
            String action = request.getParameter("action");

            // Handle password change
            if ("changePassword".equals(action)) {
                handlePasswordChange(request, response, currentUser);
                return;
            }

            // Handle profile update (default)
            handleProfileUpdate(request, response, currentUser, session);

        } catch (Exception e) {
            System.err.println("ProfileServlet Error: " + e.getMessage());
            e.printStackTrace();
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Có lỗi xảy ra: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Handle profile information update
     */
    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response, 
                                     User currentUser, HttpSession session) throws IOException {
        // Get form data
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");

        System.out.println("=== Profile Update Debug ===");
        System.out.println("User ID: " + currentUser.getUserId());
        System.out.println("FullName: " + fullName);
        System.out.println("Phone: " + phone);

        // Validation
        if (fullName == null || fullName.trim().isEmpty()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Họ và tên không được để trống\"}");
            return;
        }

        if (fullName.trim().length() < 2) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Họ và tên phải có ít nhất 2 ký tự\"}");
            return;
        }

        if (fullName.trim().length() > 100) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Họ và tên không được vượt quá 100 ký tự\"}");
            return;
        }

        // Validate phone if provided
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.trim().matches("^0[0-9]{9,10}$")) {
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Số điện thoại không hợp lệ (phải bắt đầu bằng 0 và có 10-11 chữ số)\"}");
                return;
            }
        }

        // Update user
        User updatedUser = userDAO.findById(currentUser.getUserId());
        if (updatedUser == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Không tìm thấy người dùng\"}");
            return;
        }

        updatedUser.setFullName(fullName.trim());
        updatedUser.setPhone(phone != null ? phone.trim() : "");

        System.out.println("Calling userDAO.update()...");
        boolean success = userDAO.update(updatedUser);
        System.out.println("Update result: " + success);

        if (success) {
            // Update session with new user data
            session.setAttribute("user", updatedUser);

            // Get redirect URL
            String redirectUrl = (String) session.getAttribute("redirectAfterUpdate");
            if (redirectUrl == null) {
                redirectUrl = request.getContextPath() + "/HomePage.jsp";
            }

            response.getWriter().write("{\"status\":\"success\",\"message\":\"Cập nhật thông tin thành công!\",\"redirect\":\"" + redirectUrl + "\"}");
        } else {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Không thể cập nhật thông tin. Vui lòng thử lại.\"}");
        }
    }

    /**
     * Handle password change
     */
    private void handlePasswordChange(HttpServletRequest request, HttpServletResponse response, 
                                      User currentUser) throws IOException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        System.out.println("=== Password Change Debug ===");
        System.out.println("User ID: " + currentUser.getUserId());

        // Validate current password
        if (currentPassword == null || currentPassword.isEmpty()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu hiện tại không được để trống\"}");
            return;
        }

        // Validate new password
        if (newPassword == null || newPassword.isEmpty()) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu mới không được để trống\"}");
            return;
        }

        if (newPassword.length() < 6) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu mới phải có ít nhất 6 ký tự\"}");
            return;
        }

        if (newPassword.length() > 50) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu mới không được vượt quá 50 ký tự\"}");
            return;
        }

        // Validate confirm password
        if (!newPassword.equals(confirmPassword)) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu xác nhận không khớp\"}");
            return;
        }

        // Get fresh user data with password hash
        User userWithPassword = userDAO.findById(currentUser.getUserId());
        if (userWithPassword == null) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Không tìm thấy người dùng\"}");
            return;
        }

        // Verify current password
        if (!verifyPassword(userWithPassword, currentPassword)) {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Mật khẩu hiện tại không đúng\"}");
            return;
        }

        // Update password
        boolean success = userDAO.updatePassword(userWithPassword.getUserId(), newPassword);
        
        if (success) {
            System.out.println("Password updated successfully for user: " + userWithPassword.getUserId());
            response.getWriter().write("{\"status\":\"success\",\"message\":\"Đổi mật khẩu thành công!\"}");
        } else {
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Không thể đổi mật khẩu. Vui lòng thử lại.\"}");
        }
    }

    /**
     * Verify current password
     */
    private boolean verifyPassword(User user, String plainPassword) {
        try {
            // Query the password hash directly from database
            String sql = "SELECT password_hash FROM users WHERE id = ?";
            
            try (java.sql.Connection conn = Util.DBConnection.getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, user.getUserId());
                
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password_hash");
                        return org.mindrot.jbcrypt.BCrypt.checkpw(plainPassword, storedHash);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Password verification error: " + e.getMessage());
        }
        return false;
    }
}
