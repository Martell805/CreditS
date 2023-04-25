package app.credits.exception;

import org.springframework.http.HttpStatus;

public class TariffNotFoundException extends WebExceptionWithInfo{
    public TariffNotFoundException(String info) {
        super("TARIFF_NOT_FOUND", info, HttpStatus.BAD_REQUEST);
    }
}
