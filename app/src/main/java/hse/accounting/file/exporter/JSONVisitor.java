package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONVisitor implements ExporterVisitor {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String visit(BankAccount account) {
        try {
            return mapper.writeValueAsString(account);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать объект в JSON");
            return null;
        }
    }

    @Override
    public String visit(Category category) {
        try {
            return mapper.writeValueAsString(category);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать объект в JSON");
            return null;
        }
    }

    @Override
    public String visit(Operation operation) {
        try {
            return mapper.writeValueAsString(operation);
        } catch (Exception e) {
            System.out.println("Не удалось преобразовать объект в JSON");
            return null;
        }
    }
}
