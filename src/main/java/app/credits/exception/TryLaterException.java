package app.credits.exception;

import org.springframework.http.HttpStatus;

public class TryLaterException extends WebExceptionWithInfo{
    public TryLaterException(String info) {
        super("TRY_LATER", info, HttpStatus.BAD_REQUEST);
    }
}
