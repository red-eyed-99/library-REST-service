package servlets;

import dto.author.CreateAuthorRequestDTO;
import dto.author.UpdateAuthorNameDTO;
import exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.AuthorService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorsServletTest extends BaseServletTest {

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorsServlet authorsServlet;

    @Nested
    @DisplayName("GET method tests")
    class GetMethodTests {

        @Test
        @DisplayName("Get all authors")
        void getAllAuthors_authorResponseDTOListSend() throws SQLException, IOException {
            doReturn(null).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doGet(request, response);

            var authorResponseDTOList = authorService.findAll();

            verifyResponsePrinted(authorResponseDTOList);
        }

        @Test
        @DisplayName("Get author by id")
        void getAuthorById_authorResponseDTOSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doGet(request, response);

            var authorResponseDTO = authorService.findById(anyLong());

            verifyResponsePrinted(authorResponseDTO);
        }

        @ParameterizedTest
        @DisplayName("Get author by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void getAuthorByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(400).when(response).getStatus();

            authorsServlet.doGet(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Get author by non-existent id")
        void getAuthorByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Author not found");
            doThrow(notFoundException).when(authorService).findById(anyLong());

            authorsServlet.doGet(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }
    }

    @Nested
    @DisplayName("POST method tests")
    class PostMethodTests {

        @Test
        @DisplayName("Add new author")
        void addNewAuthor_createAuthorResponseDTOSend() throws SQLException, IOException {
            doReturn("validName").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doPost(request, response);

            var createAuthorResponseDTO = authorService.create(any(CreateAuthorRequestDTO.class));

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verifyResponsePrinted(createAuthorResponseDTO);
        }

        @Test
        @DisplayName("Add new author with incorrect name")
        void addNewAuthorWithIncorrectName_sendBadRequest() throws SQLException, IOException {
            doReturn("invalidName123").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doPost(request, response);

            var badRequestResponse = new ErrorResponse(400, "first-name parameter must contain only letters");

            verifyResponsePrinted(badRequestResponse);
        }
    }

    @Nested
    @DisplayName("PATCH method tests")
    class PatchMethodTests {

        @Test
        @DisplayName("Update author name")
        void updateAuthorName_updateAuthorNameDTOSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();
            doReturn("validName").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doPatch(request, response);

            var updateAuthorNameDTO = authorService.updateName(any(UpdateAuthorNameDTO.class));

            verifyResponsePrinted(updateAuthorNameDTO);
        }

        @Test
        @DisplayName("Update author name with incorrect name")
        void updateAuthorWithIncorrectName_sendBadRequest() throws IOException {
            doReturn("/1").when(request).getPathInfo();
            doReturn("invalidName123").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doPatch(request, response);

            var badRequestResponse = new ErrorResponse(400, "first-name parameter must contain only letters");

            verifyResponsePrinted(badRequestResponse);
        }

        @ParameterizedTest
        @DisplayName("Update author name with incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void updateAuthorNameWithIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn("validName").when(request).getParameter("first-name");
            doReturn("validName").when(request).getParameter("last-name");
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doPatch(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }
    }

    @Nested
    @DisplayName("DELETE method tests")
    class DeleteMethodTests {

        @Test
        @DisplayName("Delete author by id")
        void deleteAuthorById_noContentSend() throws SQLException, IOException {
            doReturn("/1").when(request).getPathInfo();

            authorsServlet.doDelete(request, response);

            authorService.delete(anyLong());

            verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

        @ParameterizedTest
        @DisplayName("Delete author by incorrect id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void deleteAuthorByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getPathInfo();
            doReturn(400).when(response).getStatus();
            doReturn(printWriter).when(response).getWriter();

            authorsServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }

        @Test
        @DisplayName("Delete author by non-existent id")
        void deleteAuthorByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getPathInfo();
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Author not found");
            doThrow(notFoundException).when(authorService).delete(anyLong());

            authorsServlet.doDelete(request, response);

            var badRequestResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(badRequestResponse);
        }
    }
}
