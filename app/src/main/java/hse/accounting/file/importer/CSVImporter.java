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
import java.util.ArrayList;
import java.util.List;

public class CSVImporter<T> extends AbstractImporter<T> {
    private final Class<T> type;

    public CSVImporter(Class<T> type, BankAccountFacade accountFacade,
                       CategoryFacade categoryFacade, OperationFacade operationFacade) {
        super(accountFacade, categoryFacade, operationFacade);
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<T> parseData(String content) throws Exception {
        List<T> entities = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new StringReader(content))) {
            String[] line;
            reader.readNext(); // Пропускаем заголовок, если он есть
            while ((line = reader.readNext()) != null) {
                if (type == BankAccount.class) {
                    entities.add((T) new BankAccount(
                            Long.parseLong(line[0]),
                            line[1],
                            Double.parseDouble(line[2])
                    ));
                } else if (type == Category.class) {
                    entities.add((T) new Category(
                            Long.parseLong(line[0]),
                            Category.Type.valueOf(line[1]),
                            line[2]
                    ));
                } else if (type == Operation.class) {
                    entities.add((T) new Operation(
                            Long.parseLong(line[0]),
                            Operation.Type.valueOf(line[1]),
                            Long.parseLong(line[2]),
                            Double.parseDouble(line[3]),
                            LocalDateTime.parse(line[4]),
                            line[5],
                            Long.parseLong(line[6])
                    ));
                }
            }
            if (entities.isEmpty()) throw new IllegalArgumentException("Empty CSV");
            return entities;
        }
    }

    @Override
    protected void saveEntity(List<T> entities) {
        for (T entity : entities) {
            if (type == BankAccount.class) {
                BankAccount account = (BankAccount) entity;
                accountFacade.createBankAccount(account.getName(), account.getBalance());
            } else if (type == Category.class) {
                Category category = (Category) entity;
                categoryFacade.createCategory(category.getName(), category.getType());
            } else if (type == Operation.class) {
                Operation operation = (Operation) entity;
                operationFacade.createOperation(operation.getType(),
                        operation.getBankAccountId(),
                        operation.getAmount(),
                        operation.getDateTime(),
                        operation.getDescription(),
                        operation.getCategoryId());
            }
        }
    }
}