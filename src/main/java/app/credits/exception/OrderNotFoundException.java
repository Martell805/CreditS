package app.credits.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends WebExceptionWithInfo {
    public OrderNotFoundException(String message) {
        super("ORDER_NOT_FOUND", message, HttpStatus.BAD_REQUEST);
    }
}
