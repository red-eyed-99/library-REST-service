package servlets;

import dto.reader.CreateReaderRequestDTO;
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

import java.io.IOException;
import java.sql.SQLException;

import static utils.RequestParamExtractor.getIdFrom;
import static utils.validator.RequestParamValidator.*;

@WebServlet(urlPatterns = {"/readers", "/readers/*"})
public class ReadersServlet extends HttpServlet {

    private ReaderService readerService;

    @Override
    public void init() {
        readerService = (ReaderService) getServletContext().getAttribute("readerService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (request.getPathInfo() != null) {
                doGetById(request, response);
                return;
            }

            var readerResponseDTO = readerService.findAll();

            JsonResponsePrinter.print(response, readerResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    private void doGetById(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        var idParameter = getIdFrom(request.getPathInfo());

        validateId(idParameter);

        var readerId = Long.valueOf(idParameter);

        var readerResponseDTO = readerService.findById(readerId);

        JsonResponsePrinter.print(response, readerResponseDTO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var firstName = request.getParameter("first-name");
            var lastName = request.getParameter("last-name");
            var phone = request.getParameter("phone");

            validateName("first-name", firstName);
            validateName("last-name", lastName);
            validatePhone("phone", phone);

            var createReaderDTO = new CreateReaderRequestDTO(firstName, lastName, phone);

            var readerResponseDTO = readerService.create(createReaderDTO);

            response.setStatus(HttpServletResponse.SC_CREATED);

            JsonResponsePrinter.print(response, readerResponseDTO);

        } catch (SQLException | BadRequestException | AlreadyExistException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var idParameter = getIdFrom(request.getPathInfo());

            validateId(idParameter);

            var readerId = Long.valueOf(idParameter);

            readerService.delete(readerId);

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (SQLException | NotFoundException | BadRequestException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }
}
