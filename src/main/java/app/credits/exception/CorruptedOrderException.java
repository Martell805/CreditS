package app.credits.exception;

import org.springframework.http.HttpStatus;

public class CorruptedOrderException extends WebExceptionWithInfo{
    public CorruptedOrderException(String info) {
        super("CORRUPTED_ORDER", info, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
