package servlets;

import exceptions.AlreadyExistException;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ReaderService;
import utils.response.ErrorResponseSender;
import utils.response.JsonResponsePrinter;
import utils.validator.RequestParamValidator;

import java.io.IOException;
import java.sql.SQLException;

import static utils.validator.RequestParamValidator.validateId;

@WebServlet("/addBookToReader")
public class ReaderAddBookServlet extends HttpServlet {

    private ReaderService readerService;

    @Override
    public void init() {
        readerService = (ReaderService) getServletContext().getAttribute("readerService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var readerIdParameter = (String) request.getAttribute("readerId");
            var bookIdParameter = (String) request.getAttribute("bookId");

            RequestParamValidator.validateId(readerIdParameter);
            RequestParamValidator.validateId(bookIdParameter);

            var readerId = Long.valueOf(readerIdParameter);
            var bookId = Long.valueOf(bookIdParameter);

            var readerResponseDTO = readerService.addBookToReader(readerId, bookId);

            response.setStatus(HttpServletResponse.SC_CREATED);

            JsonResponsePrinter.print(response, readerResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException | AlreadyExistException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }
}
