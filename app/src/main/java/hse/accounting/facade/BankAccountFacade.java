package hse.accounting.facade;

import hse.accounting.domain.BankAccount;
import hse.accounting.factory.BankAccountFactory;
import hse.accounting.repository.InMemoryRepository;
import javafx.util.Pair;

import java.util.List;

/**
 * Фасад для работы с банковскими счетами
 */
public class BankAccountFacade {
    private final InMemoryRepository<BankAccount> repository;
    private final BankAccountFactory factory;
    private final OperationFacade operationFacade;


    public BankAccountFacade(InMemoryRepository<BankAccount> repository, BankAccountFactory factory, OperationFacade operationFacade) {
        this.repository = repository;
        this.factory = factory;
        this.operationFacade = operationFacade;
    }

    public void createBankAccount(String name, double balance) {
        BankAccount bankAccount = factory.create(name, balance);
        repository.save(bankAccount);
    }

    public BankAccount getBankAccount(Long id) {
        return repository.getById(id);
    }

    public List<Pair<Long, BankAccount>> getAllBankAccounts() {
        return repository.getList();
    }

    public void updateBankAccountName(BankAccount bankAccount, String name) {
        bankAccount.setName(name);
        repository.save(bankAccount);
    }

    public void updateBankAccountBalance(BankAccount bankAccount, double balance) {
        bankAccount.setBalance(balance);
        repository.save(bankAccount);
    }

    public void deleteBankAccount(BankAccount bankAccount) {
        repository.delete(bankAccount);
    }

    public void recalculateBalance(Long accountId) {
        BankAccount account = repository.getById(accountId);
        if (account != null) {
            double calculatedBalance = operationFacade.calculateBalanceForAccount(accountId);
            account.setBalance(calculatedBalance);
            repository.save(account);
        }
    }

}