package hse.accounting.file.importer;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.LoaderOptions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YAMLImporter<T> extends AbstractImporter<T> {
    private final Yaml yaml;
    private final Class<T> type;

    public YAMLImporter(Class<T> type, BankAccountFacade accountFacade,
                        CategoryFacade categoryFacade, OperationFacade operationFacade) {
        super(accountFacade, categoryFacade, operationFacade);
        this.type = type;
        LoaderOptions options = new LoaderOptions();
        this.yaml = new Yaml(options);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<T> parseData(String content) {
        List<T> entities = new ArrayList<>();
        Iterable<Object> yamlObjects = yaml.loadAll(content);

        for (Object obj : yamlObjects) {
            if (obj instanceof List) {
                for (Object item : (List<?>) obj) {
                    entities.add(convertToEntity(item));
                }
            } else {
                entities.add(convertToEntity(obj));
            }
        }

        if (entities.isEmpty()) {
            throw new IllegalArgumentException("Empty YAML content");
        }
        return entities;
    }

    @SuppressWarnings("unchecked")
    private T convertToEntity(Object obj) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;

            if (type == BankAccount.class) {
                return (T) new BankAccount(
                        getLongValue(map, "id"),
                        getStringValue(map, "name"),
                        getDoubleValue(map, "balance")
                );
            } else if (type == Category.class) {
                return (T) new Category(
                        getLongValue(map, "id"),
                        Category.Type.valueOf(getStringValue(map, "type", "EXPENSE")), // значение по умолчанию
                        getStringValue(map, "name")
                );
            } else if (type == Operation.class) {
                return (T) new Operation(
                        getLongValue(map, "id"),
                        Operation.Type.valueOf(getStringValue(map, "type", "EXPENSE")), // значение по умолчанию
                        getLongValue(map, "bankAccountId"),
                        getDoubleValue(map, "amount"),
                        LocalDateTime.parse(getStringValue(map, "date", LocalDateTime.now().toString())), // значение по умолчанию
                        getStringValue(map, "description"),
                        getLongValue(map, "categoryId")
                );
            }
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
        return (T) obj;
    }

    // Вспомогательные методы для безопасного получения значений
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        return value instanceof Number ? ((Number) value).longValue() : Long.parseLong(value.toString());
    }

    private Double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0.0; // или можно выбросить исключение
        return value instanceof Number ? ((Number) value).doubleValue() : Double.parseDouble(value.toString());
    }

    @Override
    protected void saveEntity(List<T> entities) {
        for (T entity : entities) {
            if (type == BankAccount.class) {
                BankAccount account = (BankAccount) entity;
                accountFacade.createBankAccount(account.getName(), account.getBalance());
            } else if (type == Category.class) {
                Category category = (Category) entity;
                categoryFacade.createCategory(category.getName(), category.getType());
            } else if (type == Operation.class) {
                Operation operation = (Operation) entity;
                operationFacade.createOperation(operation.getType(),
                        operation.getBankAccountId(),
                        operation.getAmount(),
                        operation.getDateTime(),
                        operation.getDescription(),
                        operation.getCategoryId());
            }
        }
    }
}