package hse.accounting.factory;

import hse.accounting.domain.BankAccount;
import hse.accounting.idgenerator.IdGenerator;

public class BankAccountFactory {
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public BankAccount create(String name, double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance must be non-negative");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must be non-empty");
        }

        return new BankAccount(idGenerator.generateId(), name, balance);
    }
}
