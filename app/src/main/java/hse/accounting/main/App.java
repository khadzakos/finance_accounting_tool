package hse.accounting.main;

import hse.accounting.config.ApplicationConfig;
import hse.accounting.facade.AnalyticsFacade;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;
import hse.accounting.ui.UI;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    private static BankAccountFacade bankAccountFacade;
    private static CategoryFacade categoryFacade;
    private static OperationFacade operationFacade;
    private static AnalyticsFacade analyticsFacade;

    public static void Run() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        bankAccountFacade = context.getBean(BankAccountFacade.class);
        categoryFacade = context.getBean(CategoryFacade.class);
        operationFacade = context.getBean(OperationFacade.class);
        analyticsFacade = context.getBean(AnalyticsFacade.class);

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
                case 8 -> UI.getAccountDifferenceForPeriod(analyticsFacade, bankAccountFacade);
                case 9 -> UI.groupOperationsByCategory(analyticsFacade, categoryFacade);
                case 10 -> UI.editAccount(bankAccountFacade);
                case 11 -> UI.editCategory(categoryFacade);
                case 12 -> UI.editOperation(operationFacade, categoryFacade);
                case 13 -> UI.deleteAccount(bankAccountFacade);
                case 14 -> UI.deleteCategory(categoryFacade);
                case 15 -> UI.deleteOperation(operationFacade);
//                case 16 -> UI.importExport();
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
