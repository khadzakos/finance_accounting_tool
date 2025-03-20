package hse.accounting.facade;

import hse.accounting.domain.Operation;
import hse.accounting.factory.OperationFactory;
import hse.accounting.repository.InMemoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Pair;

/**
 * Фасад для работы с операциями
 */
public class OperationFacade {
    private final InMemoryRepository<Operation> repository;
    private final OperationFactory factory;

    public OperationFacade(InMemoryRepository<Operation> repository, OperationFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public void createOperation(Operation.Type type, Long bankAccountId, double amount, LocalDateTime dateTime, String description, Long categoryId) {
        Operation operation = factory.create(type, bankAccountId, amount, dateTime, description, categoryId);
        repository.save(operation);
    }

    public Operation getOperation(Long id) {
        return repository.getById(id);
    }

    public boolean checkOperation(Long id) {
        return repository.getById(id) != null;
    }

    public List<Operation> getAllOperations() {
        return repository.getList();
    }

    public void updateOperation(Operation operation, Operation.Type type, double amount, LocalDateTime dateTime, String description) {
        operation.setType(type);
        operation.setAmount(amount);
        operation.setDateTime(dateTime);
        operation.setDescription(description);
        repository.save(operation);
    }

    public void deleteOperation(Operation operation) {
        repository.delete(operation);
    }

    public double calculateBalanceForAccount(Long accountId) {
        List<Operation> operations = repository.getList().stream()
                .filter(operation -> operation.getBankAccountId().equals(accountId))
                .toList();

        double balance = 0;
        for (Operation operation : operations) {
            if (operation.getType() == Operation.Type.INCOME) {
                balance += operation.getAmount();
            } else {
                balance -= operation.getAmount();
            }
        }

        return balance;
    }
}
