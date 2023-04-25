package app.credits.exception;

import org.springframework.http.HttpStatus;

public class PasswordCannotChangeException extends WebExceptionWithInfo{
    public PasswordCannotChangeException(String info) {
        super("CANNOT_CHANGE_PASSWORD", info, HttpStatus.FORBIDDEN);
    }
}
