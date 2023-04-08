package challengegamebackend.service;

import challengegamebackend.model.Category;
import challengegamebackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category getCategoryById(long id) {
        return checkCategoryExistsAndGet(
                categoryRepository.findById(id),
                "id",
                "" + id
        );
    }

    public Category getCategoryByName(String name) {
        return checkCategoryExistsAndGet(
                categoryRepository.findByName(name),
                "name",
                "" + name
        );
    }

    public long getIdByName(String name) {
        return getCategoryByName(name).getId();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public long getAllCategoriesCount() {
        return categoryRepository.count();
    }

    private Category checkCategoryExistsAndGet(Optional<Category> opt, String key, String value) {
        if (!opt.isPresent())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Category with " + key + ": " + value + " not found"
            );
        return opt.get();
    }

}
