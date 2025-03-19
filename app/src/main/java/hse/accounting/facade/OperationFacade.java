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

    public Operation createOperation(Operation.Type type, Long bankAccountId, double amount, LocalDateTime dateTime, String description, Long categoryId) {
        Operation operation = factory.create(type, bankAccountId, amount, dateTime, description, categoryId);
        repository.save(operation);
        return operation;
    }

    public Operation getOperation(Long id) {
        return repository.getById(id);
    }

    public List<Pair<Long, Operation>> getAllOperations() {
        return repository.getList();
    }

    public void updateOperationType(Operation operation, Operation.Type type) {
        operation.setType(type);
        repository.save(operation);
    }

    public void updateOperationAmount(Operation operation, double amount) {
        operation.setAmount(amount);
        repository.save(operation);
    }

    public void updateOperationDateTime(Operation operation, LocalDateTime dateTime) {
        operation.setDateTime(dateTime);
        repository.save(operation);
    }

    public void updateOperationDescription(Operation operation, String description) {
        operation.setDescription(description);
        repository.save(operation);
    }

    public void deleteOperation(Operation operation) {
        repository.delete(operation);
    }

    public double calculateBalanceForAccount(Long accountId) {
        List<Operation> operations = repository.getList().stream()
                .map(Pair::getValue)
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
