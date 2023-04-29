package app.credits.service;

import app.credits.enums.Role;
import app.credits.exception.PasswordCannotChangeException;
import app.credits.exception.RoleCannotChangeException;
import app.credits.exception.UserNotFoundException;
import app.credits.entity.User;
import app.credits.model.Email;
import app.credits.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MessageService messageService;

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(messageService.getMessage("exceptions.user_not_found_by_id", id))
        );
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(messageService.getMessage("exceptions.user_not_found_by_email", email))
        );
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User add(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public User edit(User user) {
        User oldUser = getById(user.getId());

        if (!oldUser.getPassword().equals(user.getPassword())) {
            throw new PasswordCannotChangeException(messageService.getMessage("exceptions.password_cannot_change"));
        }

        if (!oldUser.getRole().equals(user.getRole())) {
            throw new RoleCannotChangeException(messageService.getMessage("exceptions.cannot_change_role"));
        }

        return userRepository.update(user);
    }

    public User changePassword(Long userId, String newPassword) {
        User user = getById(userId);

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        emailService.sendIfSubscribed(user, new Email(
                messageService.getMessage("email.password_changed.subject"),
                messageService.getMessage("email.password_changed.message", user.getName())
        ));

        return userRepository.update(user);
    }

    public User changeRole(Long userId, Role newRole) {
        User user = getById(userId);

        user.setRole(newRole);

        return userRepository.update(user);
    }

    public User delete(User user) {
        userRepository.delete(user);

        return user;
    }

    public User subscribe(User user) {
        user.setEmailSubscription(true);
        return userRepository.update(user);
    }

    public User unsubscribe(User user) {
        user.setEmailSubscription(false);
        return userRepository.update(user);
    }
}
