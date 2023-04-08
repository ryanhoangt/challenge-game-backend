package challengegamebackend.dto;

import challengegamebackend.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private long id;
    private String username;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
}
