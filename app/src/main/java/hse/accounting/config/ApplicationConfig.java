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

import hse.accounting.file.importer.Importer;
import hse.accounting.file.importer.JSONImporter;
import hse.accounting.file.importer.CSVImporter;
import hse.accounting.file.importer.YAMLImporter;

import hse.accounting.file.exporter.ExporterVisitor;
import hse.accounting.file.exporter.JSONVisitor;
import hse.accounting.file.exporter.YAMLVisitor;
import hse.accounting.file.exporter.CSVVisitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Spring приложения - DI контейнер
 */
@Configuration
public class ApplicationConfig {
    @Bean
    public Importer<BankAccount> jsonBankAccountImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new JSONImporter<>(BankAccount.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<Category> jsonCategoryImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new JSONImporter<>(Category.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<Operation> jsonOperationImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new JSONImporter<>(Operation.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<BankAccount> csvBankAccountImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new CSVImporter<>(BankAccount.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<Category> csvCategoryImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new CSVImporter<>(Category.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<Operation> csvOperationImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new CSVImporter<>(Operation.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<BankAccount> yamlBankAccountImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new YAMLImporter<>(BankAccount.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public Importer<Category> yamlCategoryImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new YAMLImporter<>(Category.class, bankAccountFacade, categoryFacade, operationFacade);
    }

    @Bean
    public ExporterVisitor jsonVisitor() {
        return new JSONVisitor();
    }

    @Bean
    public ExporterVisitor csvVisitor() {
        return new CSVVisitor();
    }

    @Bean
    public ExporterVisitor yamlVisitor() {
        return new YAMLVisitor();
    }

    @Bean
    public Importer<Operation> yamlOperationImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, OperationFacade operationFacade) {
        return new YAMLImporter<>(Operation.class, bankAccountFacade, categoryFacade, operationFacade);
    }

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
        return new AnalyticsFacade(operationRepository());
    }
}