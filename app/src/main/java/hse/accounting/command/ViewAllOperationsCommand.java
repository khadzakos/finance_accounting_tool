package hse.accounting.command;

import hse.accounting.facade.OperationFacade;

public class ViewAllOperationsCommand implements Command {
    private final OperationFacade facade;

    public ViewAllOperationsCommand(OperationFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.getAllOperations().forEach(System.out::println);
    }
}
