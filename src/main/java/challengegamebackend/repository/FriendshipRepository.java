package challengegamebackend.repository;

import challengegamebackend.model.Friendship;
import challengegamebackend.model.FriendshipId;
import challengegamebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {
    public Optional<Friendship> findByUser1AndUser2(User user1, User user2);
    public List<Friendship> findByUser1(User user1);
    public List<Friendship> findByUser2(User user2);
    public void delete(Friendship friendship);
}
