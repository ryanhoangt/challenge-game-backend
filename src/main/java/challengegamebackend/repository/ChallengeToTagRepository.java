package challengegamebackend.repository;

import challengegamebackend.model.Challenge;
import challengegamebackend.model.ChallengeToTag;
import challengegamebackend.model.ChallengeToTagId;
import challengegamebackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeToTagRepository extends JpaRepository<ChallengeToTag, ChallengeToTagId> {
    public List<ChallengeToTag> findByChallenge(Challenge challenge);
    public List<ChallengeToTag> findByTag(Tag tag);
}
