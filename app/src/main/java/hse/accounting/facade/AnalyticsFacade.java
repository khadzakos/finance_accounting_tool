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
    private final InMemoryRepository<Category> categoryRepository;
    private final InMemoryRepository<Operation> operationRepository;

    public AnalyticsFacade(InMemoryRepository<Category> categoryRepository, InMemoryRepository<Operation> operationRepository) {
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public double getDifferenceForPeriod(LocalDateTime start, LocalDateTime end) {
        double income = 0;
        double expense = 0;
        for (Operation operation : operationRepository.getList().stream().map(pair -> pair.getValue()).toList()) {
            if (operation.getDateTime().isAfter(start) && operation.getDateTime().isBefore(end)) {
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
                .map(Pair::getValue)
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .toList();
    }
}
