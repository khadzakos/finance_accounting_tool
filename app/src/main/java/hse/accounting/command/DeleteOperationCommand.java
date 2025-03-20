package hse.accounting.command;

import hse.accounting.facade.OperationFacade;

public class DeleteOperationCommand implements Command {
    private final OperationFacade facade;
    private final Long operationId;

    public DeleteOperationCommand(OperationFacade facade, Long operationId) {
        this.facade = facade;
        this.operationId = operationId;
    }

    @Override
    public void execute() {
        facade.deleteOperation(facade.getOperation(operationId));
    }
}
