package app.credits.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageSource messageSource;

    @Value("${app.locale}")
    private String locale;

    public String getMessage(String name) {
        return getMessage(name, List.of());
    }

    public String getMessage(String name, Object arg0) {
        return getMessage(name, List.of(arg0));
    }

    public String getMessage(String name, Object arg0, Object arg1) {
        return getMessage(name, List.of(arg0, arg1));
    }

    public String getMessage(String name, Object arg0, Object arg1, Object arg2) {
        return getMessage(name, List.of(arg0, arg1, arg2));
    }

    public String getMessage(String name, List<Object> args) {
        return messageSource.getMessage(name, args.toArray(), new Locale(locale));
    }
}
