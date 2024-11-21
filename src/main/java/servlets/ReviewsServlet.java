package servlets;

import dto.review.CreateReviewDTO;
import exceptions.AlreadyExistException;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ReviewService;
import utils.ExtraSpaceTrimmer;
import utils.response.ErrorResponseSender;
import utils.response.JsonResponsePrinter;

import java.io.IOException;
import java.sql.SQLException;

import static utils.RequestParamExtractor.getIdValuesFrom;
import static utils.validator.RequestParamValidator.validateContent;
import static utils.validator.RequestParamValidator.validateId;

@WebServlet("/reviewsServlet")
public class ReviewsServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() {
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var bookIdParameter = getIdValuesFrom((String) request.getAttribute("pathInfo"));

            validateId(bookIdParameter[0]);

            var bookId = Long.valueOf(bookIdParameter[0]);

            var bookReviewsDTO = reviewService.findAll(bookId);

            JsonResponsePrinter.print(response, bookReviewsDTO);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var readerIdParameter = request.getParameter("reader-id");
            var bookIdParameter = getIdValuesFrom((String) request.getAttribute("pathInfo"));
            var content = request.getParameter("content");

            validateId(readerIdParameter);
            validateId(bookIdParameter[0]);

            validateContent("content", content);
            content = ExtraSpaceTrimmer.trim(content);

            var readerId = Long.valueOf(readerIdParameter);
            var bookId = Long.valueOf(bookIdParameter[0]);

            var createReviewDTO = new CreateReviewDTO(readerId, bookId, content);

            var reviewResponseDTO = reviewService.create(createReviewDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);

            JsonResponsePrinter.print(response, reviewResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException | AlreadyExistException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }
}
