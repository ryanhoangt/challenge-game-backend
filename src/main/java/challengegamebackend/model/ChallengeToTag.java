package challengegamebackend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ChallengeToTag {

    @EmbeddedId
    private ChallengeToTagId id = new ChallengeToTagId();

    @ManyToOne
    @JoinColumn(name = "challengeId", insertable=false, updatable=false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "tagId", insertable=false, updatable=false)
    private Tag tag;
}
