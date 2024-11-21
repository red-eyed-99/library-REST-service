package servlets;

import dto.book.CreateBookDTO;
import exceptions.AlreadyExistException;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.BookService;
import utils.ExtraSpaceTrimmer;
import utils.response.ErrorResponseSender;
import utils.response.JsonResponsePrinter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static utils.RequestParamExtractor.getIdFrom;
import static utils.validator.RequestParamValidator.*;

@WebServlet(urlPatterns = {"/books", "/books/*"})
public class BooksServlet extends HttpServlet {

    private BookService bookService;

    @Override
    public void init() {
        bookService = (BookService) getServletContext().getAttribute("bookService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getPathInfo() != null) {
                doGetById(request, response);
                return;
            }

            var booksResponseDTO = bookService.findAll();

            JsonResponsePrinter.print(response, booksResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    private void doGetById(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        var idParameter = getIdFrom(request.getPathInfo());

        validateId(idParameter);

        var authorId = Long.valueOf(idParameter);

        var bookResponseDTO = bookService.findById(authorId);

        JsonResponsePrinter.print(response, bookResponseDTO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var title = request.getParameter("title");
            var publishYear = request.getParameter("publish-year");
            var authorIdParameters = request.getParameterValues("authors");

            validateTitle("title", title);
            title = ExtraSpaceTrimmer.trim(title);

            validateYear("publish-year", publishYear);
            validateIdValues(authorIdParameters);

            var authorIdSet = getAuthorIdSet(authorIdParameters);

            var createBookDTO = new CreateBookDTO(title, Integer.parseInt(publishYear), authorIdSet);

            var bookResponseDTO = bookService.create(createBookDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);

            JsonResponsePrinter.print(response, bookResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException | AlreadyExistException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var idParameter = getIdFrom(request.getPathInfo());

            validateId(idParameter);

            var bookId = Long.valueOf(idParameter);

            bookService.delete(bookId);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    private Set<Long> getAuthorIdSet(String[] authorIdParameters) {
        var authorIdSet = new HashSet<Long>();

        for (var authorIdParameter : authorIdParameters) {
            authorIdSet.add(Long.valueOf(authorIdParameter));
        }

        return authorIdSet;
    }
}
