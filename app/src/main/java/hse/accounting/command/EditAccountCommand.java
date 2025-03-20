package hse.accounting.command;

import hse.accounting.domain.BankAccount;
import hse.accounting.facade.BankAccountFacade;

public class EditAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final Long accountId;
    private final String name;
    private final double balance;

    public EditAccountCommand(BankAccountFacade facade, Long accountId, String name, double balance) {
        this.facade = facade;
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public void execute() {
        BankAccount account = facade.getBankAccount(accountId);
        facade.updateBankAccount(account, name, balance);
    }

}
