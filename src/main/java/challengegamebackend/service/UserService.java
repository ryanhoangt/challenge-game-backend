package challengegamebackend.service;

import challengegamebackend.model.User;
import challengegamebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(long id) {
        return checkUserExistsAndGet(
                userRepository.findById(id),
                "id",
                "" + id
        );
    }

    public User getUserByUsername(String username) {
        return checkUserExistsAndGet(
                userRepository.findByUsername(username),
                "username",
                username);
    }

    private User checkUserExistsAndGet(Optional<User> opt, String key, String value) {
        if (!opt.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User with " + key + ": " + value + " not found"
            );
        return opt.get();
    }

    public String register(String username, String pwd, String pwdConfirm) {
        System.out.println(username + " " + pwd + " " + pwdConfirm);
        if (pwdConfirm.equals(pwd)) {
            // if the username is new
            if (!userRepository.findByUsername(username).isPresent()) {
                User user = new User();
                user.setUsername(username);
                // Store encoded passwords
                user.setPwdHash(passwordEncoder.encode(pwd));

                System.out.println(user);

                userRepository.save(user);
                return "User with username: " + username + " has been created.";
            } else
                throw new ResponseStatusException(HttpStatus.CONFLICT,"User with username: " + username + " already exists");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password confirmation does not match.");
    }
}
