package hse.accounting.command;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.facade.AnalyticsFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.BankAccountFacade;

import java.util.List;

public class GetOperationsByCategoryCommand implements Command {
    private final AnalyticsFacade facade;
    private final CategoryFacade categoryFacade;
    private BankAccountFacade bankAccountFacade;
    private final Long categoryId;

    public GetOperationsByCategoryCommand(AnalyticsFacade facade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade, Long categoryId) {
        this.facade = facade;
        this.categoryFacade = categoryFacade;
        this.bankAccountFacade = bankAccountFacade;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        List<Operation> operations = facade.getOperationsByCategory(categoryId);
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
