package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static utils.RequestParamExtractor.getIdFrom;
import static utils.RequestParamExtractor.getIdValuesFrom;

@WebFilter("/readers/*")
public class ReaderForwardFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        var request = (HttpServletRequest) servletRequest;
        var response = (HttpServletResponse) servletResponse;

        var method = request.getMethod();

        if (method.equalsIgnoreCase("PATCH")) {
            forwardToUpdateReaderPhone(request, response);
            return;
        }

        if (method.equalsIgnoreCase("POST")) {
            if (request.getPathInfo() != null) {
                forwardToAddReaderBook(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void forwardToUpdateReaderPhone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("readerId", getIdFrom(request.getPathInfo()));
        request.setAttribute("phone", request.getParameter("phone"));

        request.getRequestDispatcher("/updateReaderPhone").forward(request, response);
    }

    private void forwardToAddReaderBook(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        var pathInfo = request.getPathInfo();

        if (pathInfo.contains("/books/")) {
            var idParameters = getIdValuesFrom(request.getPathInfo());

            final int EXPECTED_ID_COUNT = 2;

            if (idParameters.length == EXPECTED_ID_COUNT) {
                var readerId = idParameters[0];
                var bookId = idParameters[1];

                request.setAttribute("readerId", readerId);
                request.setAttribute("bookId", bookId);
            }

            request.getRequestDispatcher("/addBookToReader").forward(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
