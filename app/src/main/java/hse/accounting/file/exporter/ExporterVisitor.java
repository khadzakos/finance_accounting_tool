package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

public interface ExporterVisitor {
    String visit(BankAccount account);
    String visit(Category category);
    String visit(Operation operation);
}