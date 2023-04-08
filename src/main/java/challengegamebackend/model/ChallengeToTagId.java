package challengegamebackend.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ChallengeToTagId implements Serializable {

    private static final long serialVersionUID = 1L;

    private long challengeId;
    private long tagId;

    public ChallengeToTagId() {
    }

    public ChallengeToTagId(long challengeId, long tagId) {
        super();
        this.challengeId = challengeId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChallengeToTagId that = (ChallengeToTagId) o;
        return challengeId == that.challengeId && tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(challengeId, tagId);
    }
}
