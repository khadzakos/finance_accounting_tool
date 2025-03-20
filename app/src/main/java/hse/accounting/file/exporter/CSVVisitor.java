package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

public class CSVVisitor implements ExporterVisitor {
    @Override
    public String visit(BankAccount account) {
        return account.getId() + ";" + account.getName() + ";" + account.getBalance();
    }

    @Override
    public String visit(Category category) {
        return category.getId() + ";" + category.getName() + ";" + category.getType();
    }

    @Override
    public String visit(Operation operation) {
        return operation.getId() + ";" + operation.getType() + ";" + operation.getBankAccountId() + ";" + operation.getAmount() + ";" + operation.getDateTime() + ";" + operation.getDescription() + ";" + operation.getCategoryId();
    }
}
