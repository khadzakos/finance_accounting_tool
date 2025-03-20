package hse.accounting.command;

import hse.accounting.facade.AnalyticsFacade;

public class GetIncomesCommand implements Command {
    private final AnalyticsFacade facade;

    public GetIncomesCommand(AnalyticsFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        System.out.println("Получено доходов: " + facade.getAllIncome());
    }
}
