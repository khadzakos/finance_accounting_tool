package hse.accounting.idgenerator;

/**
 * Стратегия генерации идентификаторов по возрастанию
 */
public class IncrementalIStrategy implements IIdGenerationStrategy {
    private long currentId = 0;

    @Override
    public long generateId() {
        return currentId++;
    }
}