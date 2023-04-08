package challengegamebackend.dto;

import lombok.Data;

@Data
public class UserWithCompletionCount {
    private long userId;
    private String username;
    private long completionCount;

    public UserWithCompletionCount(long userId, String username, long completionCount) {
        this.userId = userId;
        this.username = username;
        this.completionCount = completionCount;
    }
}
