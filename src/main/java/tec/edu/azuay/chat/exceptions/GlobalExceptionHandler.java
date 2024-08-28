package tec.edu.azuay.chat.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tec.edu.azuay.chat.models.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handlerObjectNotFoundException(Exception exception, HttpServletRequest request){
        ApiError error = new ApiError();

        error.setMessage("Object not found");
        error.setBackendMessage(exception.getLocalizedMessage());
        error.setUrl(request.getRequestURI());

        return ResponseEntity.status(404).body(error);
    }
}
