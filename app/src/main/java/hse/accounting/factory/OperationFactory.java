package hse.accounting.factory;

import hse.accounting.domain.Operation;
import hse.accounting.idgenerator.IdGenerator;

import java.time.LocalDateTime;

/**
 * Фабрика для создания операций
 */
public class OperationFactory {
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public Operation create(Operation.Type type, Long bankAccountId, double amount, LocalDateTime date, String description, Long categoryId) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        return new Operation(idGenerator.generateId(), type, bankAccountId, amount, date, description, categoryId);
    }
}