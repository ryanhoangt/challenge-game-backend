package challengegamebackend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class FriendshipId implements Serializable {

    private static final long serialVersionUID = 1L;

    private long user1Id;
    private long user2Id;

    public FriendshipId() {
    }

    public FriendshipId(long user1Id, long user2Id) {
        super();
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return user1Id == that.user1Id && user2Id == that.user2Id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }
}
