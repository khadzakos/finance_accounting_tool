package hse.accounting.factory;

import hse.accounting.domain.Category;
import hse.accounting.idgenerator.IdGenerator;

/**
 * Фабрика для создания категорий
 */
public class CategoryFactory {
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public Category create(String name, Category.Type type) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be non-empty");
        }

        return new Category(idGenerator.generateId(), type, name);
    }
}
