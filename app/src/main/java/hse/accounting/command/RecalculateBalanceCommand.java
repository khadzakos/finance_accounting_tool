package hse.accounting.command;

import hse.accounting.facade.BankAccountFacade;

public class RecalculateBalanceCommand implements Command {
    private final BankAccountFacade facade;
    private final Long accountId;

    public RecalculateBalanceCommand(BankAccountFacade facade, Long accountId) {
        this.facade = facade;
        this.accountId = accountId;
    }

    @Override
    public void execute() {
        facade.recalculateBalance(accountId);
    }
}
