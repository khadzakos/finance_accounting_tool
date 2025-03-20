package hse.accounting.file.importer;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import org.yaml.snakeyaml.Yaml;

public class YAMLImporter<T> extends AbstractImporter<T> {
    private final Yaml yaml = new Yaml();
    private final Class<T> type;

    public YAMLImporter(Class<T> type, BankAccountFacade accountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        super(accountFacade, categoryFacade, operationFacade);
        this.type = type;
    }

    @Override
    protected T parseData(String content) {
        return yaml.loadAs(content, type);
    }

    @Override
    protected void saveEntity(T entity) {
        if (type == BankAccount.class) {
            BankAccount account = (BankAccount) entity;
            accountFacade.createBankAccount(account.getName(), account.getBalance());
        } else if (type == Category.class) {
            Category category = (Category) entity;
            categoryFacade.createCategory(category.getName(), category.getType());
        } else if (type == Operation.class) {
            Operation operation = (Operation) entity;
            operationFacade.createOperation(operation.getType(), operation.getBankAccountId(), operation.getAmount(),
                    operation.getDateTime(), operation.getDescription(), operation.getCategoryId());
        }
    }
}
