package Controller;

import Model.User;
import DAO.UserDAO;
import Util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet for handling user registration
 */
@WebServlet("/RegisterServlet")
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        userDAO = new UserDAO();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Generate CSRF token for form
        String csrfToken = java.util.UUID.randomUUID().toString();
        session.setAttribute("_csrf_token", csrfToken);
        
        request.setAttribute("_csrf_token", csrfToken);
        
        String path = "WEB-INF/Client/Register.jsp";
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
        StringBuilder errorMessages = new StringBuilder();
        
        try {
            // Get and validate CSRF token
            String sessionCsrfToken = (String) session.getAttribute("_csrf_token");
            String requestCsrfToken = request.getParameter("_csrf_token");
            
            if (sessionCsrfToken == null || !sessionCsrfToken.equals(requestCsrfToken)) {
                errorMessages.append("Yêu cầu không hợp lệ. Vui lòng thử lại.");
                request.setAttribute("error", errorMessages.toString());
                request.getRequestDispatcher("WEB-INF/Client/Register.jsp").forward(request, response);
                return;
            }
            
            // Get form parameters
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");
            
            // Sanitize inputs
            fullName = ValidationUtil.sanitizeInput(fullName);
            email = ValidationUtil.sanitizeInput(email);
            phone = ValidationUtil.sanitizeInput(phone);
            
            // Validate full name
            if (!ValidationUtil.isValidName(fullName)) {
                errorMessages.append("Họ tên không hợp lệ. Vui lòng nhập từ 2-100 ký tự, chỉ bao gồm chữ cái và khoảng trắng.");
            }
            
            // Validate email
            if (!ValidationUtil.isValidEmail(email)) {
                if (errorMessages.length() > 0) errorMessages.append("<br>");
                errorMessages.append("Email không hợp lệ. Vui lòng nhập đúng định dạng email.");
            }
            
            // Validate phone (optional but if provided must be valid)
            if (phone != null && !phone.trim().isEmpty() && !ValidationUtil.isValidPhone(phone)) {
                if (errorMessages.length() > 0) errorMessages.append("<br>");
                errorMessages.append("Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại Việt Nam.");
            }
            
            // Validate password
            String passwordError = ValidationUtil.getPasswordErrorMessage(password);
            if (passwordError != null) {
                if (errorMessages.length() > 0) errorMessages.append("<br>");
                errorMessages.append(passwordError);
            }
            
            // Validate password match
            if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
                if (errorMessages.length() > 0) errorMessages.append("<br>");
                errorMessages.append("Mật khẩu và xác nhận mật khẩu không khớp.");
            }
            
            // If there are validation errors, show them
            if (errorMessages.length() > 0) {
                request.setAttribute("error", errorMessages.toString());
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("WEB-INF/Client/Register.jsp").forward(request, response);
                return;
            }
            
            // Check if email already exists
            if (userDAO.emailExists(email)) {
                request.setAttribute("error", "Email đã được đăng ký. Vui lòng sử dụng email khác.");
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.getRequestDispatcher("WEB-INF/Client/Register.jsp").forward(request, response);
                return;
            }
            
            // Create user object
            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole("USER");
            
            // Register user
            boolean success = userDAO.register(user, password);
            
            if (success) {
                // Registration successful - REDIRECT to login page with success message
                session.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
                
                // Clear form data from session
                session.removeAttribute("fullName");
                session.removeAttribute("email");
                session.removeAttribute("phone");
                
                // REDIRECT to LoginServlet
                response.sendRedirect(request.getContextPath() + "/LoginServlet");
            } else {
                request.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
                request.getRequestDispatcher("WEB-INF/Client/Register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            request.setAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại sau.");
            request.getRequestDispatcher("WEB-INF/Client/Register.jsp").forward(request, response);
        }
    }
}