package servlets;

import dto.reader.UpdateReaderPhoneDTO;
import exceptions.AlreadyExistException;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.ReaderService;
import utils.response.ErrorResponseSender;
import utils.response.JsonResponsePrinter;

import java.io.IOException;
import java.sql.SQLException;

import static utils.validator.RequestParamValidator.validateId;
import static utils.validator.RequestParamValidator.validatePhone;

@WebServlet("/updateReaderPhone")
public class ReaderUpdatePhoneServlet extends HttpServlet {

    private ReaderService readerService;

    @Override
    public void init() {
        readerService = (ReaderService) getServletContext().getAttribute("readerService");
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
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            var idParameter = (String) request.getAttribute("readerId");
            var phone = (String) request.getAttribute("phone");

            validateId(idParameter);
            validatePhone("phone", phone);

            var readerId = Long.valueOf(idParameter);

            var updateReaderPhoneDTO = new UpdateReaderPhoneDTO(readerId, phone);

            var readerResponseDTO = readerService.updatePhone(updateReaderPhoneDTO);

            JsonResponsePrinter.print(response, readerResponseDTO);

        } catch (SQLException | NotFoundException | BadRequestException | AlreadyExistException ex) {
            ErrorResponseSender.send(response, ex);
        }
    }
}
