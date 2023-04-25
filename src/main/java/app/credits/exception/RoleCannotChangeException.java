package app.credits.exception;

import org.springframework.http.HttpStatus;

public class RoleCannotChangeException extends WebExceptionWithInfo{
    public RoleCannotChangeException(String info) {
        super("CANNOT_CHANGE_ROLE", info, HttpStatus.FORBIDDEN);
    }
}
