package utils.response;

import exceptions.AlreadyExistException;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseSender {

    public static void send(HttpServletResponse response, Exception ex) throws IOException {

        if (ex instanceof NotFoundException) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if (ex instanceof BadRequestException) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else if (ex instanceof AlreadyExistException) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            var errorResponse = new ErrorResponse(response.getStatus(), "An error occurred while request processing");

            JsonResponsePrinter.print(response, errorResponse);
            return;
        }

        var errorResponse = new ErrorResponse(response.getStatus(), ex.getMessage());

        JsonResponsePrinter.print(response, errorResponse);
    }
}
