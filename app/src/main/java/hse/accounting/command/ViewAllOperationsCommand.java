package hse.accounting.command;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import java.util.List;

public class ViewAllOperationsCommand implements Command {
    private final OperationFacade operationFacade;
    private final CategoryFacade categoryFacade;
    private final BankAccountFacade bankAccountFacade;

    public ViewAllOperationsCommand(OperationFacade operationFacade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade) {
        this.operationFacade = operationFacade;
        this.categoryFacade = categoryFacade;
        this.bankAccountFacade = bankAccountFacade;
    }

    @Override
    public void execute() {
        List<Operation> operations = operationFacade.getAllOperations();
        for (Operation operation : operations) {
            BankAccount bankAccount = bankAccountFacade.getBankAccount(operation.getBankAccountId());
            Category category = categoryFacade.getCategory(operation.getCategoryId());
            System.out.printf("""
                    --------------------------------
                    ID: %d
                    Дата: %s
                    Сумма: %.2f
                    Категория: %s
                    Счет: %s
                    Описание: %s
                    """, operation.getId(), operation.getDateTime(), operation.getAmount(), category.getName(), bankAccount.getName(), operation.getDescription());
        }
    }
}
