package tec.edu.azuay.chat.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tec.edu.azuay.chat.models.dto.ApiError;

import java.time.LocalDateTime;
import java.util.Map;

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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handlerInvalidCredentialsException(Exception exception, HttpServletRequest request){
        ApiError error = new ApiError();

        error.setMessage("Invalid credentials");
        error.setBackendMessage(exception.getLocalizedMessage());
        error.setHttpCode(401);
        error.setUrl(request.getRequestURI());
        error.setMethod(request.getMethod());
        error.setTime(LocalDateTime.now());

        return ResponseEntity.status(401).body(error);
    }

    @ExceptionHandler(ExistsObjectException.class)
    public ResponseEntity<?> handlerExistsObjectException(Exception exception, HttpServletRequest request){
        ApiError error = new ApiError();

        error.setHttpCode(409);
        error.setMessage(exception.getMessage());
        error.setBackendMessage(exception.getLocalizedMessage());
        error.setUrl(request.getRequestURI());
        error.setMethod(request.getMethod());
        error.setTime(LocalDateTime.now());

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handlerExpiredJwt(Exception exception, HttpServletRequest request) {

        return ResponseEntity.status(401).body(Map.of("error", "Token JWT expirado"));
    }
}
