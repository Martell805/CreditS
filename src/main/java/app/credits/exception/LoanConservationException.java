package app.credits.exception;

import org.springframework.http.HttpStatus;

public class LoanConservationException extends WebExceptionWithInfo{
    public LoanConservationException(String info) {
        super("LOAN_CONSIDERATION", info, HttpStatus.BAD_REQUEST);
    }
}
