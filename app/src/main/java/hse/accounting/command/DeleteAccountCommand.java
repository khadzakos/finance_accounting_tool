package hse.accounting.command;

import hse.accounting.facade.BankAccountFacade;

public class DeleteAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final Long accountId;

    public DeleteAccountCommand(BankAccountFacade facade, Long accountId) {
        this.facade = facade;
        this.accountId = accountId;
    }

    @Override
    public void execute() {
        facade.deleteBankAccount(facade.getBankAccount(accountId));
    }
}
