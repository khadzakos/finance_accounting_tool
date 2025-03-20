package hse.accounting.main;

import hse.accounting.config.ApplicationConfig;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Category;
import hse.accounting.domain.Operation;

import hse.accounting.facade.AnalyticsFacade;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import hse.accounting.file.importer.Importer;
import hse.accounting.file.exporter.ExporterVisitor;

import hse.accounting.ui.UI;

import java.util.List;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void Run() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        BankAccountFacade bankAccountFacade = context.getBean(BankAccountFacade.class);
        CategoryFacade categoryFacade = context.getBean(CategoryFacade.class);
        OperationFacade operationFacade = context.getBean(OperationFacade.class);
        AnalyticsFacade analyticsFacade = context.getBean(AnalyticsFacade.class);
        Importer<BankAccount> jsonBankAccountImporter = context.getBean("jsonBankAccountImporter", Importer.class);
        Importer<Category> jsonCategoryImporter = context.getBean("jsonCategoryImporter", Importer.class);
        Importer<Operation> jsonOperationImporter = context.getBean("jsonOperationImporter", Importer.class);
        Importer<BankAccount> csvBankAccountImporter = context.getBean("csvBankAccountImporter", Importer.class);
        Importer<Category> csvCategoryImporter = context.getBean("csvCategoryImporter", Importer.class);
        Importer<Operation> csvOperationImporter = context.getBean("csvOperationImporter", Importer.class);
        Importer<BankAccount> yamlBankAccountImporter = context.getBean("yamlBankAccountImporter", Importer.class);
        Importer<Category> yamlCategoryImporter = context.getBean("yamlCategoryImporter", Importer.class);
        Importer<Operation> yamlOperationImporter = context.getBean("yamlOperationImporter", Importer.class);
        ExporterVisitor jsonVisitor = context.getBean("jsonVisitor", ExporterVisitor.class);
        ExporterVisitor csvVisitor = context.getBean("csvVisitor", ExporterVisitor.class);
        ExporterVisitor yamlVisitor = context.getBean("yamlVisitor", ExporterVisitor.class);

        List<Importer<BankAccount>> accountImporters = List.of(jsonBankAccountImporter, csvBankAccountImporter, yamlBankAccountImporter);
        List<Importer<Category>> categoryImporters = List.of(jsonCategoryImporter, csvCategoryImporter, yamlCategoryImporter);
        List<Importer<Operation>> operationImporters = List.of(jsonOperationImporter, csvOperationImporter, yamlOperationImporter);
        List<ExporterVisitor> exporters = List.of(jsonVisitor, csvVisitor, yamlVisitor);

        while (true) {
            UI.printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> UI.createAccount(bankAccountFacade);
                case 2 -> UI.createCategory(categoryFacade);
                case 3 -> UI.createOperation(operationFacade, categoryFacade, bankAccountFacade);
                case 4 -> UI.viewAllAccounts(bankAccountFacade);
                case 5 -> UI.viewAllCategories(categoryFacade);
                case 6 -> UI.viewAllOperations(operationFacade, categoryFacade, bankAccountFacade);
                case 7 -> UI.recalculateBalance(bankAccountFacade);
                case 8 -> UI.analitycs(analyticsFacade, categoryFacade, bankAccountFacade);
                case 9 -> UI.editAccount(bankAccountFacade);
                case 10 -> UI.editCategory(categoryFacade);
                case 11 -> UI.editOperation(operationFacade, categoryFacade);
                case 12 -> UI.deleteAccount(bankAccountFacade);
                case 13 -> UI.deleteCategory(categoryFacade, operationFacade);
                case 14 -> UI.deleteOperation(operationFacade);
                case 15 -> UI.importFile(accountImporters, categoryImporters, operationImporters);
                case 16 -> UI.exportFile(exporters);
                case 0 -> {
                    context.close();
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }
}
