package challengegamebackend.controller;

import challengegamebackend.dto.UserDTO;
import challengegamebackend.model.User;
import challengegamebackend.service.UserService;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> showRegistrationForm(
            @RequestParam String username,
            @RequestParam String pwd,
            @RequestParam String pwdConfirm) {
        return new ResponseEntity<>(userService.register(username, pwd, pwdConfirm), HttpStatus.CREATED);
    }

    @GetMapping("/api/users/myUsername")
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/api/users/myProfile")
    public UserDTO currentUserProfile(Principal principal) {
        return new UserDTO(userService.getUserByUsername(principal.getName()));
    }

    @GetMapping("/api/users")
    public List<UserDTO> allUsers() {
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User u: userService.getAllUsers())
            userDTOS.add(new UserDTO(u));
        return userDTOS;
    }

    @GetMapping("/api/users/{userId}")
    public UserDTO getUser(@PathVariable Long userId) {
        return new UserDTO(userService.getUserById(userId));
    }

    // Ex: /api/users?username=anna
    @GetMapping("/api/users/byUsername/{username}")
    public UserDTO getUserByName(@PathVariable String username) {
        return new UserDTO(userService.getUserByUsername(username));
    }

}
