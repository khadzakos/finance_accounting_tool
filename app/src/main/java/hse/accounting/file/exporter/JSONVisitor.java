package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JSONVisitor implements ExporterVisitor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String visitBankAccounts(List<BankAccount> accounts) {
        try {
            return mapper.writeValueAsString(accounts);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать список банковских счетов в JSON");
            return "[]";
        }
    }

    @Override
    public String visitCategories(List<Category> categories) {
        try {
            return mapper.writeValueAsString(categories);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать список категорий в JSON");
            return "[]";
        }
    }

    @Override
    public String visitOperations(List<Operation> operations) {
        try {
            return mapper.writeValueAsString(operations);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать список операций в JSON");
            return "[]";
        }
    }
}