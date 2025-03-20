package hse.accounting.command;

import hse.accounting.domain.BankAccount;
import hse.accounting.facade.BankAccountFacade;
import javafx.util.Pair;

import java.util.List;

public class ViewAllAccountsCommand implements Command {
    private final BankAccountFacade facade;

    public ViewAllAccountsCommand(BankAccountFacade facade) {
        this.facade = facade;
    }

    @Override
    public void execute() {
        List<BankAccount> accounts = facade.getAllBankAccounts();
        for (BankAccount account : accounts) {
            System.out.printf("""
                    --------------------------------
                    ID: %d
                    Название счета: %s
                    Баланс: %.2f
                    """, account.getId(), account.getName(), account.getBalance());
        }
    }

}
