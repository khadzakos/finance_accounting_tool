package hse.accounting.command;

import hse.accounting.facade.BankAccountFacade;

public class ViewAllAccountsCommand implements Command {
    private final BankAccountFacade facade;

    public ViewAllAccountsCommand(BankAccountFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        facade.getAllBankAccounts().forEach(System.out::println);
    }

}
