package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import java.util.List;

public class CSVVisitor implements ExporterVisitor {
    private static final String DELIMITER = ",";
    private static final String NEW_LINE = "\n";

    @Override
    public String visitBankAccounts(List<BankAccount> accounts) {
        StringBuilder sb = new StringBuilder();
        sb.append("id").append(DELIMITER)
                .append("name").append(DELIMITER)
                .append("balance").append(NEW_LINE);

        for (BankAccount account : accounts) {
            sb.append(account.getId()).append(DELIMITER)
                    .append(escape(account.getName())).append(DELIMITER)
                    .append(account.getBalance()).append(NEW_LINE);
        }
        return sb.toString();
    }

    @Override
    public String visitCategories(List<Category> categories) {
        StringBuilder sb = new StringBuilder();
        sb.append("id").append(DELIMITER)
                .append("type").append(DELIMITER)
                .append("name").append(NEW_LINE);

        for (Category category : categories) {
            sb.append(category.getId()).append(DELIMITER)
                    .append(category.getType()).append(DELIMITER)
                    .append(escape(category.getName())).append(NEW_LINE);
        }
        return sb.toString();
    }

    @Override
    public String visitOperations(List<Operation> operations) {
        StringBuilder sb = new StringBuilder();
        sb.append("id").append(DELIMITER)
                .append("type").append(DELIMITER)
                .append("bankAccountId").append(DELIMITER)
                .append("amount").append(DELIMITER)
                .append("date").append(DELIMITER)
                .append("description").append(DELIMITER)
                .append("categoryId").append(NEW_LINE);

        for (Operation operation : operations) {
            sb.append(operation.getId()).append(DELIMITER)
                    .append(operation.getType()).append(DELIMITER)
                    .append(operation.getBankAccountId()).append(DELIMITER)
                    .append(operation.getAmount()).append(DELIMITER)
                    .append(operation.getDateTime()).append(DELIMITER)
                    .append(escape(operation.getDescription())).append(DELIMITER)
                    .append(operation.getCategoryId()).append(NEW_LINE);
        }
        return sb.toString();
    }

    // Экранирование значений, содержащих разделители или кавычки
    private String escape(String value) {
        if (value == null) return "";
        if (value.contains(DELIMITER) || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}