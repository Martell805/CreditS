package app.credits.exception;

import org.springframework.http.HttpStatus;

public class LoanAlreadyApprovedException extends WebExceptionWithInfo{
    public LoanAlreadyApprovedException(String info) {
        super("LOAN_ALREADY_APPROVED", info, HttpStatus.BAD_REQUEST);
    }
}
