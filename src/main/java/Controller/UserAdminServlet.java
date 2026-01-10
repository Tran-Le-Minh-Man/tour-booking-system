package Controller;

import Model.User;
import DAO.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing users (Admin)
 * Author: MiniMax Agent
 */
@WebServlet("/Admin/UserServlet")
public class UserAdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;
    
    public UserAdminServlet() {
        super();
        userDAO = new UserDAO();
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
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "updateRole":
                updateRole(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }
    
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requireAdmin(request, response);
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "updateRole":
                updateRole(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }
    
    /**
     * List all users with filter and search
     * SỬA: Thêm xử lý filter và search
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy tham số filter và search
            String roleFilter = request.getParameter("role");
            String searchTerm = request.getParameter("search");
            
            // Lưu lại giá trị filter để hiển thị trong form
            request.setAttribute("roleFilter", roleFilter);
            request.setAttribute("searchTerm", searchTerm);
            
            List<User> users;
            
            // Xử lý tìm kiếm và lọc
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Tìm kiếm theo từ khóa
                users = userDAO.searchUsers(searchTerm.trim());
                
                // Nếu có cả search và filter
                if (roleFilter != null && !roleFilter.isEmpty()) {
                    users = userDAO.filterUsersByRoleAndSearch(roleFilter, searchTerm.trim());
                }
            } else if (roleFilter != null && !roleFilter.isEmpty()) {
                // Chỉ lọc theo quyền
                users = userDAO.getUsersByRole(roleFilter);
            } else {
                // Lấy tất cả
                users = userDAO.getAllUsers();
            }
            
            int totalUsers = userDAO.getTotalCount();
            int adminCount = userDAO.countByRole("ADMIN");
            int userCount = userDAO.countByRole("USER");
            
            // Get current admin to prevent self-deletion
            User currentAdmin = (User) request.getSession(false).getAttribute("user");
            
            request.setAttribute("users", users);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("adminCount", adminCount);
            request.setAttribute("userCount", userCount);
            request.setAttribute("currentAdminId", currentAdmin != null ? currentAdmin.getUserId() : -1);
            
            request.getRequestDispatcher("/WEB-INF/Admin/UserList.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error listing users: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải danh sách người dùng.");
            request.getRequestDispatcher("/WEB-INF/Admin/UserList.jsp").forward(request, response);
        }
    }
    
    /**
     * Update user role
     */
    private void updateRole(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String newRole = request.getParameter("role");
            
            // Prevent changing own role
            User currentAdmin = (User) request.getSession(false).getAttribute("user");
            if (currentAdmin != null && currentAdmin.getUserId() == userId) {
                request.setAttribute("error", "Bạn không thể thay đổi quyền của chính mình.");
                listUsers(request, response);
                return;
            }
            
            if (newRole == null || (!newRole.equals("ADMIN") && !newRole.equals("USER"))) {
                request.setAttribute("error", "Quyền không hợp lệ.");
                listUsers(request, response);
                return;
            }
            
            boolean success = userDAO.updateRole(userId, newRole);
            
            if (success) {
                request.setAttribute("success", "Cập nhật quyền người dùng thành công!");
            } else {
                request.setAttribute("error", "Cập nhật quyền người dùng thất bại.");
            }
            
            listUsers(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID người dùng không hợp lệ.");
            listUsers(request, response);
        }
    }
    
    /**
     * Delete user
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            
            // Prevent deleting yourself
            User currentAdmin = (User) request.getSession(false).getAttribute("user");
            if (currentAdmin != null && currentAdmin.getUserId() == userId) {
                request.setAttribute("error", "Bạn không thể xóa chính mình.");
                listUsers(request, response);
                return;
            }
            
            boolean success = userDAO.delete(userId);
            
            if (success) {
                request.setAttribute("success", "Xóa người dùng thành công!");
            } else {
                request.setAttribute("error", "Xóa người dùng thất bại.");
            }
            
            listUsers(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID người dùng không hợp lệ.");
            listUsers(request, response);
        }
    }
}