package utils.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonResponsePrinter {

    public static void print(HttpServletResponse response, Object object) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        var printWriter = response.getWriter();

        var mapper = new ObjectMapper();

        var jsonResponse = mapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(object);

        printWriter.print(jsonResponse);
        printWriter.flush();
    }
}
