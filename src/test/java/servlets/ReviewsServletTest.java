package servlets;

import dto.review.CreateReviewDTO;
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
import services.ReviewService;
import utils.response.ErrorResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewsServletTest extends BaseServletTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewsServlet reviewsServlet;

    @Nested
    @DisplayName("GET method tests")
    class GetMethodTests {

        @Test
        @DisplayName("Get all reviews by book id")
        void getAllReviewsByBookId_reviewResponseDTOListSend() throws SQLException, IOException {
            doReturn("/1").when(request).getAttribute("pathInfo");
            doReturn(printWriter).when(response).getWriter();

            reviewsServlet.doGet(request, response);

            var bookResponseDTOList = reviewService.findAll(anyLong());

            verifyResponsePrinted(bookResponseDTOList);
        }

        @Test
        @DisplayName("Get reviews by non-existent book id")
        void getBookByNonExistentId_sendNotFound() throws IOException, SQLException {
            doReturn("/1").when(request).getAttribute("pathInfo");
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Book not found");
            doThrow(notFoundException).when(reviewService).findAll(anyLong());

            reviewsServlet.doGet(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }

        @ParameterizedTest
        @DisplayName("Get reviews by incorrect book id")
        @CsvFileSource(resources = "/incorrect_path_info_id_params.csv", numLinesToSkip = 1)
        void getBookByIncorrectId_sendBadRequest(String idParameter, String errorMessage) throws IOException {
            doReturn(idParameter).when(request).getAttribute("pathInfo");
            doReturn(printWriter).when(response).getWriter();
            doReturn(400).when(response).getStatus();

            reviewsServlet.doGet(request, response);

            var badRequestResponse = new ErrorResponse(400, errorMessage);

            verifyResponsePrinted(badRequestResponse);
        }
    }

    @Nested
    @DisplayName("POST method tests")
    class PostMethodTests {

        @Test
        @DisplayName("Add new book review")
        void addNewBookReview_createReviewDTOSend() throws SQLException, IOException {
            doReturn("1").when(request).getParameter("reader-id");
            doReturn("/1").when(request).getAttribute("pathInfo");
            doReturn("dummy").when(request).getParameter("content");
            doReturn(printWriter).when(response).getWriter();

            reviewsServlet.doPost(request, response);

            var createBookDTO = reviewService.create(any(CreateReviewDTO.class));

            verify(response).setStatus(HttpServletResponse.SC_CREATED);
            verifyResponsePrinted(createBookDTO);
        }

        @Test
        @DisplayName("Add new book review by non-existent reader id")
        void addBookReviewByNonExistentReaderId_sendNotFound() throws IOException, SQLException {
            doReturn("1").when(request).getParameter("reader-id");
            doReturn("/1").when(request).getAttribute("pathInfo");
            doReturn("dummy").when(request).getParameter("content");
            doReturn(printWriter).when(response).getWriter();
            doReturn(404).when(response).getStatus();

            var notFoundException = new NotFoundException("Reader not found");
            doThrow(notFoundException).when(reviewService).create(any(CreateReviewDTO.class));

            reviewsServlet.doPost(request, response);

            var notFoundResponse = new ErrorResponse(404, notFoundException.getMessage());

            verifyResponsePrinted(notFoundResponse);
        }

        @ParameterizedTest
        @DisplayName("Add new book review with incorrect parameters")
        @MethodSource(value = "servlets.ReviewsServletTest#getIncorrectReviewParams")
        void addNewBookWithIncorrectParams_sendBadRequest(String readerId, String pathInfoBookId, String content,
                                                          String expectedErrorMessage) throws IOException {

            doReturn(readerId).when(request).getParameter("reader-id");
            doReturn(pathInfoBookId).when(request).getAttribute("pathInfo");
            doReturn(content).when(request).getParameter("content");
            doReturn(printWriter).when(response).getWriter();
            doReturn(400).when(response).getStatus();

            reviewsServlet.doPost(request, response);

            var badRequestResponse = new ErrorResponse(400, expectedErrorMessage);

            verifyResponsePrinted(badRequestResponse);
        }
    }

    static Stream<Arguments> getIncorrectReviewParams() {
        return Stream.of(
                Arguments.of("-1", "/1", "dummy", "id must be a positive number"),
                Arguments.of("0", "/1", "dummy", "id must be a positive number"),
                Arguments.of("id", "/1", "dummy", "id must be a number"),
                Arguments.of("1", "/1", null, "Missing content parameter")
        );
    }
}
