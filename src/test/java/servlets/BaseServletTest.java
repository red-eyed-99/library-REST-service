package servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mock;

import java.io.PrintWriter;

import static org.mockito.Mockito.verify;

abstract class BaseServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter printWriter;

    void verifyResponsePrinted(Object expectedResponse) throws JsonProcessingException {
        var mapper = new ObjectMapper();

        var jsonResponse = mapper.writeValueAsString(expectedResponse);

        verify(printWriter).print(jsonResponse);
    }
}
