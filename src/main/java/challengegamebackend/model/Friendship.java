package challengegamebackend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Friendship {

    @EmbeddedId
    private FriendshipId id = new FriendshipId();

    @ManyToOne
    @JoinColumn(name = "user1Id", insertable=false, updatable=false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2Id", insertable=false, updatable=false)
    private User user2;

    /**
     * false: when user1 ask for friendship but user2 didn't reply for the moment
     * true : when user1 and user2 are friends
     */
    private boolean isAccepted;

    public boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
