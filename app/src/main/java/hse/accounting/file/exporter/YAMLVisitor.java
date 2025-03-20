package hse.accounting.file.exporter;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import org.yaml.snakeyaml.Yaml;

public class YAMLVisitor implements ExporterVisitor {
    private final Yaml yaml = new Yaml();

    @Override
    public String visit(BankAccount account) {
        return yaml.dumpAsMap(account);
    }

    @Override
    public String visit(Category category) {
        return yaml.dumpAsMap(category);
    }

    @Override
    public String visit(Operation operation) {
        return yaml.dumpAsMap(operation);
    }
}
