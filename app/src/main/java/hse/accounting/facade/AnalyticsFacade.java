package hse.accounting.facade;

import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.repository.InMemoryRepository;

import javafx.util.Pair;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Фасад для работы с аналитикой
 */
public class AnalyticsFacade {
    private final InMemoryRepository<Operation> operationRepository;

    public AnalyticsFacade(InMemoryRepository<Operation> operationRepository) {
        this.operationRepository = operationRepository;
    }

    public double getAllIncome() {
        return operationRepository.getList().stream()
                .filter(operation -> operation.getType() == Operation.Type.INCOME)
                .mapToDouble(Operation::getAmount)
                .sum();
    }

    public double getAllExpense() {
        return operationRepository.getList().stream()
                .filter(operation -> operation.getType() == Operation.Type.EXPENSE)
                .mapToDouble(Operation::getAmount)
                .sum();
    }

    public double getAccountDifferenceForPeriod(Long bankAccountId, LocalDateTime start, LocalDateTime end) {
        double income = 0;
        double expense = 0;
        for (Operation operation : operationRepository.getList().stream().toList()) {
            if (operation.getBankAccountId().equals(bankAccountId) && operation.getDateTime().isAfter(start) && operation.getDateTime().isBefore(end)) {
                if (operation.getType() == Operation.Type.INCOME) {
                    income += operation.getAmount();
                } else {
                    expense += operation.getAmount();
                }
            }
        }
        return income - expense;
    }

    public List<Operation> getOperationsByCategory(Long categoryId) {
        return operationRepository.getList().stream()
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .toList();
    }
}
