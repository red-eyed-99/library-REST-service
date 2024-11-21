package servlets;

import dto.book.CreateBookDTO;
import exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.BookService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServletTest extends BaseServletTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BooksServlet booksServlet;

    @Nested
    @DisplayName("GET method tests")
    class GetMethodTests {

        @Test
        @DisplayName("Get all books")
        void getAllBooks_bookResponseDTOListSend() throws SQLException, IOException {
            doReturn(null).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            booksServlet.doGet(request, response);

            var bookResponseDTOList = bookService.findAll();

            verifyResponsePrinted(bookResponseDTOList);
        }

        @Test
        @DisplayName("Get book by id")
        void getBookById_bookResponseDTOSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            booksServlet.doGet(request, response);

            var bookResponseDTO = bookService.findById(anyLong());

            verifyResponsePrinted(bookResponseDTO);
        }

        @ParameterizedTest
        @DisplayName("Get book by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void getBookByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(400).when(response).getStatus();

            booksServlet.doGet(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Get book by non-existent id")
        void getBookByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Book not found");
            doThrow(notFoundException).when(bookService).findById(anyLong());

            booksServlet.doGet(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }
    }

    @Nested
    @DisplayName("POST method tests")
    class PostMethodTests {

        @Test
        @DisplayName("Add new book")
        void addNewBook_createBookDTOSend() throws SQLException, IOException {
            doReturn("validTitle").when(request).getParameter("title");
            doReturn("2024").when(request).getParameter("publish-year");
            doReturn(new String[]{"1", "2"}).when(request).getParameterValues("authors");
            doReturn(printWriter).when(response).getWriter();

            booksServlet.doPost(request, response);

            var createBookDTO = bookService.create(any(CreateBookDTO.class));

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verifyResponsePrinted(createBookDTO);
        }

        @ParameterizedTest
        @DisplayName("Add new book with incorrect parameters")
        @MethodSource(value = "servlets.BooksServletTest#getIncorrectBookParams")
        void addNewBookWithIncorrectParams_sendBadRequest(String title, String publishYear, String[] authors,
                                                          String expectedErrorMessage) throws IOException {

            doReturn(title).when(request).getParameter("title");
            doReturn(publishYear).when(request).getParameter("publish-year");
            doReturn(authors).when(request).getParameterValues("authors");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            booksServlet.doPost(request, response);

            var badRequestResponse = new ErrorResponse(400, expectedErrorMessage);

            verifyResponsePrinted(badRequestResponse);
        }
    }

    static Stream<Arguments> getIncorrectBookParams() {
        return Stream.of(
                Arguments.of("title!-+", "2024", new String[]{"1"}, "Only letters and numbers are allowed in the title parameter"),
                Arguments.of("title", "11111", new String[]{"1", "2"}, "publish-year parameter must contain only 4 numbers"),
                Arguments.of("title", "2024", new String[]{"incorrectId"}, "id must be a number"),
                Arguments.of("title", "2024", new String[]{"-1"}, "id must be a positive number"),
                Arguments.of("title", "2024", null, "Missing authors id parameter")
        );
    }

    @Nested
    @DisplayName("DELETE method tests")
    class DeleteMethodTests {

        @Test
        @DisplayName("Delete book by id")
        void deleteBookById_noContentSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();

            booksServlet.doDelete(request, response);

            bookService.delete(anyLong());

            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        @ParameterizedTest
        @DisplayName("Delete book by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void deleteBookByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            booksServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Delete book by non-existent id")
        void deleteBookByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Book not found");
            doThrow(notFoundException).when(bookService).delete(anyLong());

            booksServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(badRequestResponse);
        }
    }
}
