package app.credits.repository;

import app.credits.entity.User;
import app.credits.exception.OrderNotFoundException;
import app.credits.exception.UserNotFoundException;
import app.credits.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final MessageService messageService;

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", BeanPropertyRowMapper.newInstance(User.class));
    }

    public Optional<User> findById(Long id) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE id=?", BeanPropertyRowMapper.newInstance(User.class),
                    id
            );
            return Optional.ofNullable(user);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", BeanPropertyRowMapper.newInstance(User.class),
                    email
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public User save(User user) {
        jdbcTemplate.update("INSERT INTO users(username, password, email, name, passport, role, email_subscription) VALUES(?, ?, ?, ?, ?, ?, ?)",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getPassport(),
                user.getRole(),
                user.getEmailSubscription()
        );

        return findByEmail(user.getEmail()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.user_not_found_by_email", user.getEmail()))
        );
    }

    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET username=?, password=?, email=?, name=?, passport=?, role=?, email_subscription=? WHERE id=?",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getPassport(),
                user.getRole(),
                user.getEmailSubscription(),
                user.getId()
        );

        return findById(user.getId()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.user_not_found_by_id", user.getId()))
        );
    }

    public User delete(User user) {
        User oldUser = findByEmail(user.getEmail()).orElseThrow(
                () -> new OrderNotFoundException(messageService.getMessage("exceptions.user_not_found_by_email", user.getEmail()))
        );

        jdbcTemplate.update("DELETE FROM users WHERE id=?",
                user.getId()
        );

        return oldUser;
    }
}
