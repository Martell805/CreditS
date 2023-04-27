package app.credits.controller;

import app.credits.entity.User;
import app.credits.exception.NoPermissionException;
import app.credits.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/loan-service")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("user/me")
    public ResponseEntity<User> getMe(Authentication authentication){
        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("user")
    public ResponseEntity<User> post(@RequestBody User user){
        return ResponseEntity.ok(userService.add(user));
    }

    @PatchMapping("user")
    public ResponseEntity<User> patch(@RequestBody User user){
        return ResponseEntity.ok(userService.edit(user));
    }

    @PatchMapping("user/me")
    public ResponseEntity<User> patch(@RequestBody User user,
                                      Authentication authentication){
        User currentUser = (User) authentication.getPrincipal();

        if (!currentUser.getId().equals(user.getId())) {
            throw new NoPermissionException("У вас нет прав на изменение других пользователей");
        }

        return ResponseEntity.ok(userService.edit(user));
    }

    @PatchMapping("user/me/changePassword")
    public ResponseEntity<User> changeMyPassword(@RequestParam String newPassword,
                                                 Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.changePassword(user.getId(), newPassword));
    }

    @PatchMapping("user/{id}/changePassword")
    public ResponseEntity<User> changePassword(@PathVariable Long id,
                                               @RequestParam String newPassword){
        return ResponseEntity.ok(userService.changePassword(id, newPassword));
    }

    @PatchMapping("user/{id}/changeRole")
    public ResponseEntity<User> changeRole(@PathVariable Long id,
                                           @RequestParam String newRole){
        return ResponseEntity.ok(userService.changeRole(id, newRole));
    }

    @DeleteMapping("user/me")
    public ResponseEntity<User> deleteMe(@RequestBody User user,
                                         Authentication authentication){
        User currentUser = (User) authentication.getPrincipal();

        if (!currentUser.getId().equals(user.getId())) {
            throw new NoPermissionException("У вас нет прав на удаление других пользователей");
        }

        return ResponseEntity.ok(userService.delete(user));
    }

    @DeleteMapping("user")
    public ResponseEntity<User> delete(@RequestBody User user){
        return ResponseEntity.ok(userService.delete(user));
    }

    @PatchMapping("user/me/subscribe")
    public ResponseEntity<User> subscribe(Authentication authentication){
        return ResponseEntity.ok(userService.subscribe((User) authentication.getPrincipal()));
    }

    @PatchMapping("user/me/unsubscribe")
    public ResponseEntity<User> unsubscribe(Authentication authentication){
        return ResponseEntity.ok(userService.unsubscribe((User) authentication.getPrincipal()));
    }
}
