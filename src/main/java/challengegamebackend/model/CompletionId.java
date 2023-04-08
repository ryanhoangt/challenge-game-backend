package challengegamebackend.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class CompletionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private long userId;
    private long challengeId;

    public CompletionId() {
    }

    public CompletionId(long userId, long challengeId) {
        super();
        this.userId = userId;
        this.challengeId = challengeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompletionId that = (CompletionId) o;
        return userId == that.userId && challengeId == that.challengeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, challengeId);
    }
}
