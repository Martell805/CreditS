package app.credits.controllerAdvice;

import app.credits.exception.*;
import app.credits.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(WebExceptionWithInfo.class)
    public ResponseEntity<ErrorResponse> handleWebExceptionWithInfo(WebExceptionWithInfo e) {
        return new ResponseEntity<>(new ErrorResponse(
                e.getMessage(),
                e.getInfo()
        ), e.getHttpStatus());
    }
}
