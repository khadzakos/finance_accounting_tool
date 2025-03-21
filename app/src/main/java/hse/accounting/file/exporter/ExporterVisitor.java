package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import java.util.List;

public interface ExporterVisitor {
    String visitBankAccounts(List<BankAccount> accounts);
    String visitCategories(List<Category> categories);
    String visitOperations(List<Operation> operations);
}