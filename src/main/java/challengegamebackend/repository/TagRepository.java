package challengegamebackend.repository;

import challengegamebackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    public Optional<Tag> findById(long tagId);
    public Optional<Tag> findByName(String name);
}
