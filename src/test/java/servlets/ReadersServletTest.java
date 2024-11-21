package servlets;

import dto.reader.CreateReaderRequestDTO;
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
import services.ReaderService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadersServletTest extends BaseServletTest {

    @Mock
    private ReaderService readerService;

    @InjectMocks
    private ReadersServlet readersServlet;

    @Nested
    @DisplayName("GET method tests")
    class GetMethodTests {

        @Test
        @DisplayName("Get all readers")
        void getAllReaders_readerResponseDTOListSend() throws IOException, SQLException {
            doReturn(null).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            readersServlet.doGet(request, response);

            var readerResponseDTOList = readerService.findAll();

            verifyResponsePrinted(readerResponseDTOList);
        }

        @Test
        @DisplayName("Get reader by id")
        void getReaderById_readerResponseDTOSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            readersServlet.doGet(request, response);

            var readerResponseDTO = readerService.findById(anyLong());

            verifyResponsePrinted(readerResponseDTO);
        }

        @ParameterizedTest
        @DisplayName("Get reader by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void getReaderByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(400).when(response).getStatus();

            readersServlet.doGet(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Get reader by non-existent id")
        void getReaderByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Reader not found");
            doThrow(notFoundException).when(readerService).findById(anyLong());

            readersServlet.doGet(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }
    }

    @Nested
    @DisplayName("POST method tests")
    class PostMethodTests {

        @Test
        @DisplayName("Add new reader")
        void addNewReader_createReaderResponseDTOSend() throws SQLException, IOException {
            doReturn("validName").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn("+7(989)-564-19-88").when(request).getParameter("phone");
            doReturn(printWriter).when(response).getWriter();

            readersServlet.doPost(request, response);

            var createReaderResponseDTO = readerService.create(any(CreateReaderRequestDTO.class));

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verifyResponsePrinted(createReaderResponseDTO);
        }

        @ParameterizedTest
        @DisplayName("Add new reader with incorrect parameters")
        @MethodSource(value = "servlets.ReadersServletTest#getIncorrectReaderParams")
        void addNewReaderWithIncorrectParams_sendBadRequest(String firstName, String lastName, String phone,
                                                            String expectedErrorMessage) throws IOException {

            doReturn(firstName).when(request).getParameter("first-name");
            doReturn(lastName).when(request).getParameter("last-name");
            doReturn(phone).when(request).getParameter("phone");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            readersServlet.doPost(request, response);

            var badRequestResponse = new ErrorResponse(400, expectedErrorMessage);

            verifyResponsePrinted(badRequestResponse);
        }
    }

    static Stream<Arguments> getIncorrectReaderParams() {
        return Stream.of(
                Arguments.of("invalidName123", "validName", "+7(989)-564-19-88", "first-name parameter must contain only letters"),
                Arguments.of("validName", "invalidName123", "+7(989)-564-19-88", "last-name parameter must contain only letters"),
                Arguments.of("validName", "validName", "invalidPhone", "The phone number must be in this format: +7(xxx)-xxx-xx-xx"),
                Arguments.of(null, "validName", "+7(989)-564-19-88", "Missing first-name parameter")
        );
    }

    @Nested
    @DisplayName("DELETE method tests")
    class DeleteMethodTests {

        @Test
        @DisplayName("Delete reader by id")
        void deleteReaderById_noContentSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();

            readersServlet.doDelete(request, response);

            readerService.delete(anyLong());

            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        @ParameterizedTest
        @DisplayName("Delete reader by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void deleteReaderByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            readersServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Delete reader by non-existent id")
        void deleteReaderByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Reader not found");
            doThrow(notFoundException).when(readerService).delete(anyLong());

            readersServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(badRequestResponse);
        }
    }
}
