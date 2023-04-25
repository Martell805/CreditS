package app.credits.service;

import app.credits.exception.PasswordCannotChangeException;
import app.credits.exception.RoleCannotChangeException;
import app.credits.exception.UserNotFoundException;
import app.credits.entity.User;
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

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id " + id + " не найден")
        );
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User add(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole("USER");

        return userRepository.save(user);
    }

    public User edit(User user) {
        User oldUser = getById(user.getId());

        if (!oldUser.getPassword().equals(user.getPassword())) {
            throw new PasswordCannotChangeException("Пароль должен быть изменён отдельно");
        }

        if (!oldUser.getRole().equals(user.getRole())) {
            throw new RoleCannotChangeException("Роль может быть изменена только администратором");
        }

        return userRepository.update(user);
    }

    public User changePassword(Long userId, String newPassword) {
        User user = getById(userId);

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        return userRepository.update(user);
    }

    public User changeRole(Long userId, String newRole) {
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
