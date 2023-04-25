package app.credits.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends WebExceptionWithInfo{
    public UserNotFoundException(String info) {
        super("USER_NOT_FOUND", info, HttpStatus.BAD_REQUEST);
    }
}
