package hse.accounting.config;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;
import hse.accounting.facade.AnalyticsFacade;
import hse.accounting.factory.BankAccountFactory;
import hse.accounting.factory.CategoryFactory;
import hse.accounting.factory.OperationFactory;
import hse.accounting.repository.InMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Spring приложения - DI контейнер
 */
@Configuration
public class ApplicationConfig {
    @Bean
    public InMemoryRepository<BankAccount> bankAccountRepository() {
        return new InMemoryRepository<>();
    }

    @Bean
    public InMemoryRepository<Category> categoryRepository() {
        return new InMemoryRepository<>();
    }

    @Bean
    public InMemoryRepository<Operation> operationRepository() {
        return new InMemoryRepository<>();
    }

    @Bean
    public BankAccountFactory bankAccountFactory() {
        return new BankAccountFactory();
    }

    @Bean
    public CategoryFactory categoryFactory() {
        return new CategoryFactory();
    }

    @Bean
    public OperationFactory operationFactory() {
        return new OperationFactory();
    }

    @Bean
    public CategoryFacade categoryFacade() {
        return new CategoryFacade(categoryRepository(), categoryFactory());
    }

    @Bean
    public OperationFacade operationFacade() {
        return new OperationFacade(operationRepository(), operationFactory());
    }

    @Bean
    public BankAccountFacade bankAccountFacade() {
        return new BankAccountFacade(bankAccountRepository(), bankAccountFactory(), operationFacade());
    }

    @Bean
    public AnalyticsFacade analyticsFacade() {
        return new AnalyticsFacade(categoryRepository(), operationRepository());
    }
}