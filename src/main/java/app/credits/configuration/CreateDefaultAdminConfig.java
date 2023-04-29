package app.credits.configuration;

import app.credits.entity.User;
import app.credits.enums.Role;
import app.credits.exception.UserNotFoundException;
import app.credits.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CreateDefaultAdminConfig {
    private final UserService userService;

    @PostConstruct
    public void createAdmin() {
        User admin;

        try {
            admin = userService.getByEmail("admin@admin.ru");
        } catch (UserNotFoundException e) {
            admin = userService.add(
                    new User(
                            0L,
                            "Admin",
                            "admin",
                            "admin@admin.ru",
                            "",
                            "",
                            Role.ADMIN,
                            false
                    )
            );
            userService.changeRole(admin.getId(), Role.ADMIN);
        }

        userService.changeRole(admin.getId(), Role.ADMIN);
    }
}
