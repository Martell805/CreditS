package app.credits.exception;

public class RoleCannotChangeException extends RuntimeException {
    public RoleCannotChangeException(String message) {
        super(message);
    }
}
