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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReaderForwardFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    RequestDispatcher requestDispatcher;

    @InjectMocks
    private ReaderForwardFilter readerForwardFilter;

    @Test
    @DisplayName("When request method is PATCH, it's forwarded to ReaderUpdatePhoneServlet")
    void shouldForwardToReaderUpdatePhoneServlet() throws IOException, ServletException {
        doReturn("PATCH").when(request).getMethod();
        doReturn("/1").when(request).getPathInfo();
        doReturn(requestDispatcher).when(request).getRequestDispatcher("/updateReaderPhone");

        readerForwardFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("When request method is POST and URL contains two id's, they're set to request attributes")
    void shouldSetIdRequestAttributes() throws IOException, ServletException {
        doReturn("POST").when(request).getMethod();
        doReturn("/1/books/1").when(request).getPathInfo();
        doReturn(requestDispatcher).when(request).getRequestDispatcher("/addBookToReader");

        readerForwardFilter.doFilter(request, response, filterChain);

        verify(request, times(2)).setAttribute(anyString(), anyString());
    }

    @Test
    @DisplayName("When request method is POST and URL contains /books/ it's forwarded to ReaderAddBookServlet")
    void shouldForwardToReaderAddBookServlet() throws IOException, ServletException {
        doReturn("POST").when(request).getMethod();
        doReturn("/books/").when(request).getPathInfo();
        doReturn(requestDispatcher).when(request).getRequestDispatcher("/addBookToReader");

        readerForwardFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("When request method is POST and URL not contains /books/, response is 404")
    void shouldSetResponseStatus404() throws IOException, ServletException {
        doReturn("POST").when(request).getMethod();
        doReturn("").when(request).getPathInfo();

        readerForwardFilter.doFilter(request, response, filterChain);

        verify(requestDispatcher, never()).forward(request, response);
        verify(response).setStatus(404);
    }

}
