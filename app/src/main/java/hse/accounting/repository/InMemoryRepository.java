package hse.accounting.repository;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Шаблонный репозиторий для хранения объектов в памяти
 */
public class InMemoryRepository<T> {
    private final Map<Long, T> items = new HashMap<>();

    public void save(T item) {
        Long id;
        if (item instanceof BankAccount) {
            id = ((BankAccount) item).getId();
        } else if (item instanceof Category) {
            id = ((Category) item).getId();
        } else if (item instanceof Operation) {
            id = ((Operation) item).getId();
        } else {
            throw new IllegalArgumentException("Unknown type: " + item.getClass().getName());
        }
        items.put(id, item);
    }

    public void delete(T item) {
        Long id;
        if (item instanceof BankAccount) {
            id = ((BankAccount) item).getId();
        } else if (item instanceof Category) {
            id = ((Category) item).getId();
        } else if (item instanceof Operation) {
            id = ((Operation) item).getId();
        } else {
            throw new IllegalArgumentException("Unknown type: " + item.getClass().getName());
        }
        items.remove(id);
    }

    public T getById(Long id) {
        return items.get(id);
    }

    public List<T> getList() {
        return new ArrayList<>(items.values());
    }
}
