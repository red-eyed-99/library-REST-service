package servlets;

import exceptions.AlreadyExistException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ReaderService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderAddBookServletTest extends BaseServletTest {

    @Mock
    private ReaderService readerService;

    @InjectMocks
    private ReaderAddBookServlet readerAddBookServlet;

    @Nested
    @DisplayName("POST method tests")
    class PostMethodTests {

        @Test
        @DisplayName("Add book to reader")
        void addBookToReader_readerResponseDTOSend() throws SQLException, IOException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn("1").when(request).getAttribute("bookId");
            doReturn(printWriter).when(response).getWriter();

            readerAddBookServlet.doPost(request, response);

            var readerResponseDTO = readerService.addBookToReader(anyLong(), anyLong());

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verifyResponsePrinted(readerResponseDTO);
        }

        @ParameterizedTest
        @DisplayName("Add book to reader with incorrect parameters")
        @MethodSource(value = "servlets.ReaderAddBookServletTest#getIncorrectIdParams")
        void addNewReaderWithIncorrectParams_sendBadRequest(String readerId, String bookId,
                                                            String expectedErrorMessage) throws IOException {

            doReturn(readerId).when(request).getAttribute("readerId");
            doReturn(bookId).when(request).getAttribute("bookId");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            readerAddBookServlet.doPost(request, response);

            var badRequestResponse = new ErrorResponse(400, expectedErrorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Add already exist book to reader")
        void addAlreadyExistBookToReader_sendConflict() throws SQLException, IOException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn("1").when(request).getAttribute("bookId");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            var alreadyExistException = new AlreadyExistException("Reader already has this book");
            doThrow(alreadyExistException).when(readerService).addBookToReader(anyLong(), anyLong());

            readerAddBookServlet.doPost(request, response);

            var conflictResponse = new ErrorResponse(400, alreadyExistException.getMessage());

            verifyResponsePrinted(conflictResponse);
        }
    }

    static Stream<Arguments> getIncorrectIdParams() {
        return Stream.of(
                Arguments.of("-1", "1", "id must be a positive number"),
                Arguments.of("1", "0", "id must be a positive number"),
                Arguments.of("incorrectId", "1", "id must be a number"),
                Arguments.of("1", null, "Missing id parameter")
        );
    }
}
