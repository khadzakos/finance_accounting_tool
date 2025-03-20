package hse.accounting.file.importer;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import com.opencsv.CSVReader;
import java.io.StringReader;
import java.time.LocalDateTime;

public class CSVImporter<T> extends AbstractImporter<T> {
    private final Class<T> type;

    public CSVImporter(Class<T> type, BankAccountFacade accountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        super(accountFacade, categoryFacade, operationFacade);
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T parseData(String content) throws Exception {
        try (CSVReader reader = new CSVReader(new StringReader(content))) {
            String[] line = reader.readNext();
            if (line == null) throw new IllegalArgumentException("Empty CSV");

            if (type == BankAccount.class) {
                return (T) new BankAccount(Long.parseLong(line[0]), line[1], Double.parseDouble(line[2]));
            } else if (type == Category.class) {
                return (T) new Category(Long.parseLong(line[0]), Category.Type.valueOf(line[1]), line[2]);
            } else if (type == Operation.class) {
                return (T) new Operation(Long.parseLong(line[0]), Operation.Type.valueOf(line[1]),
                        Long.parseLong(line[2]), Double.parseDouble(line[3]), LocalDateTime.parse(line[4]),
                        line[5], Long.parseLong(line[6]));
            }
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
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