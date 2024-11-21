package servlets;

import dto.reader.UpdateReaderPhoneDTO;
import exceptions.AlreadyExistException;
import exceptions.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ReaderService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class ReaderUpdatePhoneServletTest extends BaseServletTest {

    @Mock
    private ReaderService readerService;

    @InjectMocks
    private ReaderUpdatePhoneServlet readerUpdatePhoneServlet;

    @Nested
    @DisplayName("PATCH method tests")
    class PatchMethodTests {

        @Test
        @DisplayName("Update reader phone by id")
        void updateReaderPhoneById_updateReaderPhoneDTOSend() throws SQLException, IOException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn("+7(111)-991-19-99").when(request).getAttribute("phone");
            doReturn(printWriter).when(response).getWriter();

            readerUpdatePhoneServlet.doPatch(request, response);

            var updateAuthorNameDTO = readerService.updatePhone(any(UpdateReaderPhoneDTO.class));

            verifyResponsePrinted(updateAuthorNameDTO);
        }

        @Test
        @DisplayName("Update reader phone by non-existent id")
        void updateReaderPhoneByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn("+7(111)-991-19-99").when(request).getAttribute("phone");
            doReturn(404).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            var notFoundException = new NotFoundException("Reader not found");
            doThrow(notFoundException).when(readerService).updatePhone(any(UpdateReaderPhoneDTO.class));

            readerUpdatePhoneServlet.doPatch(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }

        @ParameterizedTest
        @DisplayName("Update author name with incorrect phone")
        @ValueSource(strings = {"123", "7(999)-999-99-99", "+7999-999-99-99", "+7(999)-999-99-999"})
        void updateAuthorWithIncorrectName_sendBadRequest(String phone) throws IOException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn(phone).when(request).getAttribute("phone");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            readerUpdatePhoneServlet.doPatch(request, response);

            var badRequestResponse = new ErrorResponse(400, "The phone number must be in this format: +7(xxx)-xxx-xx-xx");

            verifyResponsePrinted(badRequestResponse);
        }

        @ParameterizedTest
        @DisplayName("Update reader phone with incorrect id")
        @CsvFileSource(resources = "/incorrect_id_params.csv", numLinesToSkip = 1)
        void updateAuthorNameWithIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getAttribute("readerId");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            readerUpdatePhoneServlet.doPatch(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Update already taken phone number")
        void updateReaderPhoneWithAlreadyTakenNumber_sendConflict() throws SQLException, IOException {
            doReturn("1").when(request).getAttribute("readerId");
            doReturn("+7(999)-999-99-99").when(request).getAttribute("phone");
            doReturn(409).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            var alreadyExistException = new AlreadyExistException("This number is already taken");
            doThrow(alreadyExistException).when(readerService).updatePhone(any(UpdateReaderPhoneDTO.class));

            readerUpdatePhoneServlet.doPatch(request, response);

            var conflictResponse = new ErrorResponse(409, alreadyExistException.getMessage());

            verifyResponsePrinted(conflictResponse);
        }
    }
}
