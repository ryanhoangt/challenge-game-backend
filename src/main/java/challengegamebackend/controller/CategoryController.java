package challengegamebackend.controller;

import challengegamebackend.model.Category;
import challengegamebackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Adds a category to the database
     *
     * @param category
     * @return
     */
    @PostMapping("/api/categories")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    /**
     * @param id id of the category
     * @return the category corresponding to the id
     */
    @GetMapping("/api/categories/{id}")
    public Category getCategoryById(@PathVariable long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * Ex: /api/categories?name=sport
     *
     * @param name name of the category, which is unique
     * @return the category corresponding to the name
     */
    @GetMapping("/api/categories/byName/{name}")
    public Category getCategoryByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    /**
     * @param
     * @return a list with all the categories
     */
    @GetMapping("/api/categories")
    public List<Category> allCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * @param
     * @return a long, the number of categories
     */
    @GetMapping("/api/categories/count")
    public long numberOfCategories() {
        return categoryService.getAllCategoriesCount();
    }

}
