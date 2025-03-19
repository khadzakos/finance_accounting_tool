package hse.accounting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Category - класс, описывающий категорию операции.
 * Примеры категорий: "Кафе" или "Здоровье" – для расходов. "Зарплата" или "Кэшбэк" – для доходов
 */
public class Category {
    @JsonProperty("categoryId")
    private final Long id;
    @JsonProperty("categoryType")
    private final Type type;
    @JsonProperty("categoryName")
    private String name;

    public enum Type { INCOME, EXPENSE }

    public Category(Long id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }


    public Long getId() { return id; }
    public Type getType() { return type; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}