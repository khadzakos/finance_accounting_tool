package hse.accounting.facade;

import hse.accounting.domain.Category;
import hse.accounting.factory.CategoryFactory;
import hse.accounting.repository.InMemoryRepository;

import java.util.List;
import javafx.util.Pair;

/**
 * Фасад для работы с категориями
 */
public class CategoryFacade {
    private final InMemoryRepository<Category> repository;
    private final CategoryFactory factory;

    public CategoryFacade(InMemoryRepository<Category> repository, CategoryFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public void createCategory(String name, Category.Type type) {
        Category category = factory.create(name, type);
        repository.save(category);
    }

    public Category getCategory(Long id) {
        return repository.getById(id);
    }

    public boolean checkCategory(Long id) {
        return repository.getById(id) != null;
    }

    public List<Category> getAllCategories() {
        return repository.getList();
    }

    public void updateCategoryName(Category category, String name) {
        category.setName(name);
        repository.save(category);
    }

    public void deleteCategory(Category category) {
        repository.delete(category);
    }
}
