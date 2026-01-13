package Controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter to protect user pages (booking, profile, etc.)
 */
@WebFilter({"/BookingServlet", "/booking/*", "/user/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        // Check if user is logged in
        if (session == null || session.getAttribute("user") == null) {
            // Store the requested URL for redirect after login
            String requestURI = httpRequest.getRequestURI();
            String queryString = httpRequest.getQueryString();
            String fullURL = requestURI + (queryString != null ? "?" + queryString : "");

            session = httpRequest.getSession(true);
            session.setAttribute("redirectUrl", fullURL);

            // Redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/LoginServlet");
            return;
        }

        // User is logged in, continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup
    }
}
