package hse.accounting.idgenerator;

/**
 * Синглтон для генерации идентификаторов
 */
public class IdGenerator {
    private static final IdGenerator INSTANCE = new IdGenerator();
    private IIdGenerationStrategy strategy;

    public IdGenerator() {
        this.strategy = new IncrementalIStrategy();
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public void setStrategy(IIdGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public long generateId() {
        return strategy.generateId();
    }
}