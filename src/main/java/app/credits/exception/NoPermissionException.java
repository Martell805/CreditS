package app.credits.exception;

import org.springframework.http.HttpStatus;

public class NoPermissionException extends WebExceptionWithInfo{
    public NoPermissionException(String info) {
        super("NO_PERMISSION", info, HttpStatus.FORBIDDEN);
    }
}
