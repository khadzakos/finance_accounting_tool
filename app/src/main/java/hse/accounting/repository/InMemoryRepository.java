package hse.accounting.repository;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import static org.springframework.core.OrderComparator.sort;

/**
 * Шаблонный репозиторий для хранения объектов в памяти
 */
public class InMemoryRepository<T> {
    private final Map<Long, T> items = new HashMap<>();

    public void save(T item) {
        switch (item.getClass().getSimpleName()) {
            case "BankAccount":
                BankAccount bankAccount = (BankAccount) item;
                items.put(bankAccount.getId(), item);
                break;
            case "Category":
                Category category = (Category) item;
                items.put(category.getId(), item);
                break;
            case "Operation":
                Operation operation = (Operation) item;
                items.put(operation.getId(), item);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    public void delete(T item) {
        switch (item.getClass().getSimpleName()) {
            case "BankAccount":
                BankAccount bankAccount = (BankAccount) item;
                items.remove(bankAccount.getId());
                break;
            case "Category":
                Category category = (Category) item;
                items.remove(category.getId());
                break;
            case "Operation":
                Operation operation = (Operation) item;
                items.remove(operation.getId());
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    public List<Pair<Long, T>> getList() {
        List<Pair<Long, T>> list = new ArrayList<>();
        for (Map.Entry<Long, T> entry : items.entrySet()) {
            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        sort(list);
        return list;
    }

    public T getById(long id) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new IllegalArgumentException("Item not found");
        }
    }
}
