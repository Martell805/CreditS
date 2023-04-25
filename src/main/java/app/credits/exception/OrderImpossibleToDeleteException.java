package app.credits.exception;

import org.springframework.http.HttpStatus;

public class OrderImpossibleToDeleteException extends WebExceptionWithInfo{
    public OrderImpossibleToDeleteException(String info) {
        super("ORDER_IMPOSSIBLE_TO_DELETE", info, HttpStatus.BAD_REQUEST);
    }
}
