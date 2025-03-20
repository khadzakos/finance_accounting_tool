package hse.accounting.command;

import hse.accounting.domain.Operation;
import hse.accounting.facade.OperationFacade;

import java.time.LocalDateTime;

public class EditOperationCommand implements Command {
    private final OperationFacade facade;
    private final Long operationId;
    private final Operation.Type operationType;
    private final double amount;
    private final LocalDateTime date;
    private final String description;

    public EditOperationCommand(OperationFacade facade, Long operationId, Operation.Type operationType, double amount, LocalDateTime date, String description) {
        this.facade = facade;
        this.operationId = operationId;
        this.operationType = operationType;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    @Override
    public void execute() {
        facade.updateOperation(facade.getOperation(operationId), operationType, amount, date, description);
    }
}
