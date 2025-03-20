package hse.accounting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * Operation - класс, описывающий операции над счетом.
*/
public class Operation {
    private final Long id;
    @JsonProperty("type")
    private Type type;
    @JsonProperty("bankAccountId")
    private Long bankAccountId;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("date")
    private LocalDateTime date;
    @JsonProperty("description")
    private String description;
    @JsonProperty("categoryId")
    private Long categoryId;


    public enum Type { INCOME, EXPENSE }

    public Operation(Long id, Type type, Long bankAccountId, double amount, LocalDateTime date, String description, Long categoryId) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Long getId() { return id; }
    public Type getType() { return type; }
    public Long getBankAccountId() { return bankAccountId; }
    public double getAmount() { return amount; }
    public LocalDateTime getDateTime() { return date; }
    public String getDescription() { return description; }
    public Long getCategoryId() { return categoryId; }

    public void setType(Type type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDateTime(LocalDateTime date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
}
