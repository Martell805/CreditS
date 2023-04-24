package app.credits.exception;

public class PasswordCannotChangeException extends RuntimeException {
    public PasswordCannotChangeException(String message) {
        super(message);
    }
}
