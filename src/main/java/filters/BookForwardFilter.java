package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/books/*")
public class BookForwardFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        var pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.contains("/reviews")) {
            request.setAttribute("pathInfo", pathInfo);
            request.getRequestDispatcher("/reviewsServlet").forward(request, response);
        }

        filterChain.doFilter(request, response);
    }
}
