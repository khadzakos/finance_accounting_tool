package hse.accounting.command;

import hse.accounting.facade.BankAccountFacade;

public class CreateAccountCommand implements Command {
    private final BankAccountFacade facade;
    private final String name;
    private final double balance;

    public CreateAccountCommand(BankAccountFacade facade, String name, double balance) {
        this.facade = facade;
        this.name = name;
        this.balance = balance;
    }

    @Override
    public void execute() {
        facade.createBankAccount(name, balance);
    }
}
