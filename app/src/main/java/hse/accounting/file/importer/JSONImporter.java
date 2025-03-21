package hse.accounting.file.importer;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class JSONImporter<T> extends AbstractImporter<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> type;

    public JSONImporter(Class<T> type, BankAccountFacade accountFacade,
                        CategoryFacade categoryFacade, OperationFacade operationFacade) {
        super(accountFacade, categoryFacade, operationFacade);
        this.type = type;
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<T> parseData(String content) throws Exception {
        if (type == BankAccount.class) {
            return (List<T>) mapper.readValue(content, new TypeReference<List<BankAccount>>(){});
        } else if (type == Category.class) {
            return (List<T>) mapper.readValue(content, new TypeReference<List<Category>>(){});
        } else if (type == Operation.class) {
            return (List<T>) mapper.readValue(content, new TypeReference<List<Operation>>(){});
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
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