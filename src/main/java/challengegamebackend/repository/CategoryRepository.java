package challengegamebackend.repository;

import challengegamebackend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Optional<Category> findById(long id);
    public Optional<Category> findByName(String name);
    public long count();
}
