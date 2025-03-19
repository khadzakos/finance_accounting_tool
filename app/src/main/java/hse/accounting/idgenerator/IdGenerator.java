package hse.accounting.idgenerator;

public class IdGenerator {
    private static final IdGenerator INSTANCE = new IdGenerator();
    private IdGenerationStrategy strategy;

    private IdGenerator() {
        this.strategy = new IncrementalIdStrategy();
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public void setStrategy(IdGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public long generateId() {
        return strategy.generateId();
    }
}