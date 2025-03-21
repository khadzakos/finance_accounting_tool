package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import org.yaml.snakeyaml.Yaml;

import java.util.List;

public class YAMLVisitor implements ExporterVisitor {
    private final Yaml yaml = new Yaml();

    @Override
    public String visitBankAccounts(List<BankAccount> accounts) {
        return yaml.dump(accounts); // dump создает YAML для списка объектов
    }

    @Override
    public String visitCategories(List<Category> categories) {
        return yaml.dump(categories);
    }

    @Override
    public String visitOperations(List<Operation> operations) {
        return yaml.dump(operations);
    }
}