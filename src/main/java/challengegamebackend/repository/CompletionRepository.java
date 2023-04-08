package challengegamebackend.repository;

import challengegamebackend.model.Challenge;
import challengegamebackend.model.Completion;
import challengegamebackend.model.CompletionId;
import challengegamebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompletionRepository extends JpaRepository<Completion, CompletionId> {
    public List<Completion> findByUser(User user);
    public List<Completion> findByChallenge(Challenge challenge);
    public Optional<Completion> findByUserAndChallenge(User user, Challenge challenge);
}
