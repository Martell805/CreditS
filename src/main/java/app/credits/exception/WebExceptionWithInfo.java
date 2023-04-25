package app.credits.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class WebExceptionWithInfo extends ExceptionWithInfo{
    private final HttpStatus httpStatus;

    public WebExceptionWithInfo(String code, String info, HttpStatus httpStatus) {
        super(code, info);
        this.httpStatus = httpStatus;
    }
}
