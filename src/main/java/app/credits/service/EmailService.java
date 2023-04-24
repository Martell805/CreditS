package app.credits.service;

import app.credits.entity.User;
import app.credits.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender emailSender;

    @Async
    public void sendIfSubscribed(User user, Email email) {
        if (!user.getEmailSubscription()) {
            return;
        }

        sendEmail(user.getEmail(), email);
    }

    @Async
    public void sendEmail(String emailAddress, Email email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailAddress);
        simpleMailMessage.setSubject(email.getSubject());
        simpleMailMessage.setText(email.getMessage());
        emailSender.send(simpleMailMessage);
    }
}
