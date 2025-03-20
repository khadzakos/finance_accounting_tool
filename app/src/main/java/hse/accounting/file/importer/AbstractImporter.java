package hse.accounting.file.importer;

import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import java.io.File;
import java.nio.file.Files;

public abstract class AbstractImporter<T> implements Importer<T> {
    protected final BankAccountFacade accountFacade;
    protected final CategoryFacade categoryFacade;
    protected final OperationFacade operationFacade;

    public AbstractImporter(BankAccountFacade accountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        this.accountFacade = accountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }

    @Override
    public void importData(File file) throws Exception {
        String content = readFile(file);
        T entity = parseData(content);
        saveEntity(entity);
    }

    protected String readFile(File file) throws Exception {
        return new String(Files.readAllBytes(file.toPath()));
    }

    protected abstract T parseData(String content) throws Exception;

    protected abstract void saveEntity(T entity);
}
