package hse.accounting.idgenerator;

public class IncrementalIdStrategy implements IdGenerationStrategy {
    private long currentId = 0;

    @Override
    public long generateId() {
        return currentId++;
    }
}