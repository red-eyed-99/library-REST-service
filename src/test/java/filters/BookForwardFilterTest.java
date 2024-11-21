package filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BookForwardFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    private BookForwardFilter bookForwardFilter;

    @Test
    @DisplayName("When request url contains /reviews, it's forward to ReviewsServlet")
    void shouldForwardToReviewsServlet() throws IOException, ServletException {
        doReturn("/reviews").when(request).getPathInfo();
        doReturn(requestDispatcher).when(request).getRequestDispatcher("/reviewsServlet");

        bookForwardFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher).forward(request, response);
    }
}
