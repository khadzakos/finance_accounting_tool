package hse.accounting.command;

import hse.accounting.facade.AnalyticsFacade;

public class GetOperationsByCategoryCommand implements Command {
    private final AnalyticsFacade facade;
    private final Long categoryId;

    public GetOperationsByCategoryCommand(AnalyticsFacade facade, Long categoryId) {
        this.facade = facade;
        this.categoryId = categoryId;
    }

    @Override
    public void execute() {
        facade.getOperationsByCategory(categoryId).forEach(System.out::println);
    }
}
