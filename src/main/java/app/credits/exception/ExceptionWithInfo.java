package app.credits.exception;

import lombok.Getter;

@Getter
public abstract class ExceptionWithInfo extends RuntimeException{
    private final String info;

    public ExceptionWithInfo(String code, String info) {
        super(code);
        this.info = info;
    }
}
