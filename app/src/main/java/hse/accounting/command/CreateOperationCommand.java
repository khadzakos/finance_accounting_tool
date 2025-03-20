package hse.accounting.command;

import hse.accounting.domain.Operation;
import hse.accounting.facade.OperationFacade;

import java.time.LocalDateTime;

public class CreateOperationCommand implements Command {
    private final OperationFacade facade;
    private final Operation.Type operationType;
    private final Long accountId;
    private final double amount;
    private final LocalDateTime date;
    private final String description;
    private final Long categoryId;

    public CreateOperationCommand(OperationFacade facade, Operation.Type operationType, Long accountId, double amount, LocalDateTime date, String description, Long categoryId) {
        this.facade = facade;
        this.operationType = operationType;
        this.accountId = accountId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        facade.createOperation(operationType, accountId, amount, date, description, categoryId);
    }

}
