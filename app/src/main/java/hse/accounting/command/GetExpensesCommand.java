package hse.accounting.command;

import hse.accounting.facade.AnalyticsFacade;

public class GetExpensesCommand implements Command {
    private final AnalyticsFacade facade;

    public GetExpensesCommand(AnalyticsFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        System.out.println("Сумма расходов: " + facade.getAllExpense());
    }
}
