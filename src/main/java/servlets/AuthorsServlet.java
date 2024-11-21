package servlets;

import dto.author.CreateAuthorRequestDTO;
import dto.author.UpdateAuthorNameDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.AuthorService;
import utils.response.ErrorResponseSender;
import utils.response.JsonResponsePrinter;
import utils.validator.RequestParamValidator;

import java.io.IOException;
import java.sql.SQLException;

import static utils.RequestParamExtractor.getIdFrom;
import static utils.validator.RequestParamValidator.validateName;

@WebServlet(urlPatterns = {"/authors", "/authors/*"})
public class AuthorsServlet extends HttpServlet {

    private AuthorService authorService;

    @Override
    public void init() {
        authorService = (AuthorService) getServletContext().getAttribute("authorService");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        var method = request.getMethod();

        if (!method.equalsIgnoreCase("PATCH")) {
            super.service(request, response);
            return;
        }

        this.doPatch(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getPathInfo() != null) {
                doGetById(request, response);
                return;
            }

            var authorsResponseDTO = authorService.findAll();

            JsonResponsePrinter.print(response, authorsResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    private void doGetById(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        var idParameter = getIdFrom(request.getPathInfo());

        RequestParamValidator.validateId(idParameter);

        var authorId = Long.valueOf(idParameter);

        var authorResponseDTO = authorService.findById(authorId);

        JsonResponsePrinter.print(response, authorResponseDTO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var firstName = request.getParameter("first-name");
            var lastName = request.getParameter("last-name");

            validateName("first-name", firstName);
            validateName("last-name", lastName);

            var createAuthorDTO = new CreateAuthorRequestDTO(firstName, lastName);

            var authorResponseDTO = authorService.create(createAuthorDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);

            JsonResponsePrinter.print(response, authorResponseDTO);

        } catch (SQLException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var idParameter = getIdFrom(request.getPathInfo());
            var firstName = request.getParameter("first-name");
            var lastName = request.getParameter("last-name");

            RequestParamValidator.validateId(idParameter);
            validateName("first-name", firstName);
            validateName("last-name", lastName);

            var authorId = Long.valueOf(idParameter);

            var updateAuthorDTO = new UpdateAuthorNameDTO(authorId, firstName, lastName);

            var authorResponseDTO = authorService.updateName(updateAuthorDTO);

            JsonResponsePrinter.print(response, authorResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var idParameter = getIdFrom(request.getPathInfo());

            RequestParamValidator.validateId(idParameter);

            var authorId = Long.valueOf(idParameter);

            authorService.delete(authorId);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }
}
