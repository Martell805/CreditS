package app.credits.controller;

import app.credits.entity.User;
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

    @GetMapping("users/{id}")
    public ResponseEntity<User> get(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping("users")
    public ResponseEntity<User> post(@RequestBody User user){
        return ResponseEntity.ok(userService.add(user));
    }

    @PatchMapping("users")
    public ResponseEntity<User> patch(@RequestBody User user){
        return ResponseEntity.ok(userService.edit(user));
    }

    @PatchMapping("users/{id}/changePassword")
    public ResponseEntity<User> changePassword(@PathVariable Long id,
                                               @RequestParam String newPassword){
        return ResponseEntity.ok(userService.changePassword(id, newPassword));
    }

    @PatchMapping("users/{id}/changeRole")
    public ResponseEntity<User> changeRole(@PathVariable Long id,
                                           @RequestParam String newRole){
        return ResponseEntity.ok(userService.changeRole(id, newRole));
    }

    @DeleteMapping("users")
    public ResponseEntity<User> delete(@RequestBody User user){
        return ResponseEntity.ok(userService.delete(user));
    }

    @PatchMapping("users/me/subscribe")
    public ResponseEntity<User> subscribe(Authentication authentication){
        return ResponseEntity.ok(userService.subscribe((User) authentication.getPrincipal()));
    }

    @PatchMapping("users/me/unsubscribe")
    public ResponseEntity<User> unsubscribe(Authentication authentication){
        return ResponseEntity.ok(userService.unsubscribe((User) authentication.getPrincipal()));
    }
}
