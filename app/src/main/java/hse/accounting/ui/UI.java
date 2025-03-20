package hse.accounting.ui;

import hse.accounting.facade.AnalyticsFacade;
import hse.accounting.facade.BankAccountFacade;
import hse.accounting.facade.CategoryFacade;
import hse.accounting.facade.OperationFacade;

import hse.accounting.domain.BankAccount;
import hse.accounting.domain.Operation;
import hse.accounting.domain.Category;

import hse.accounting.command.Command;
import hse.accounting.command.TimingDecorator;
import hse.accounting.command.CreateAccountCommand;
import hse.accounting.command.CreateCategoryCommand;
import hse.accounting.command.CreateOperationCommand;
import hse.accounting.command.DeleteAccountCommand;
import hse.accounting.command.DeleteCategoryCommand;
import hse.accounting.command.DeleteOperationCommand;
import hse.accounting.command.EditAccountCommand;
import hse.accounting.command.EditCategoryCommand;
import hse.accounting.command.EditOperationCommand;
import hse.accounting.command.GetAccountDifferenceForPeriodCommand;
import hse.accounting.command.GetIncomesCommand;
import hse.accounting.command.GetExpensesCommand;
import hse.accounting.command.GetOperationsByCategoryCommand;
import hse.accounting.command.RecalculateBalanceCommand;
import hse.accounting.command.ViewAllAccountsCommand;
import hse.accounting.command.ViewAllCategoriesCommand;
import hse.accounting.command.ViewAllOperationsCommand;
import hse.accounting.file.exporter.ExporterVisitor;
import hse.accounting.file.importer.Importer;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;


/**
 * Класс для работы с пользовательским интерфейсом
 */
public class UI {
    private static final Scanner scanner = new Scanner(System.in);

    public static void printMenu() {
        System.out.println("\n=== Меню приложения ===");
        System.out.println("1. Создать счет");
        System.out.println("2. Создать категорию");
        System.out.println("3. Создать операцию");
        System.out.println("4. Показать все счета");
        System.out.println("5. Показать все категории");
        System.out.println("6. Показать все операции");
        System.out.println("7. Пересчитать баланс счета");
         System.out.println("8. Аналитика");
        System.out.println("9. Редактировать счёт");
        System.out.println("10. Редактировать категорию");
        System.out.println("11. Редактировать операцию");
        System.out.println("12. Удалить счёт");
        System.out.println("13. Удалить категорию");
        System.out.println("14. Удалить операцию");
        System.out.println("15. Импорт данных");
        System.out.println("16. Экспорт данных");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    public static void createAccount(BankAccountFacade bankAccountFacade) {
        System.out.print("Введите название счёта: ");
        String name = scanner.nextLine();
        System.out.print("Введите баланс счёта(Пример: 100): ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Неверный формат баланса");
            scanner.nextLine();
        }
        double balance = scanner.nextDouble();
        scanner.nextLine();
        Command command = new CreateAccountCommand(bankAccountFacade, name, balance);
        command.execute();
    }

    public static void createCategory(CategoryFacade categoryFacade) {
        System.out.print("Введите название категории: ");
        String name = scanner.nextLine();

        System.out.print("""
                Введите тип категории:
                1. INCOME
                2. EXPENSE
                """);
        String typeAsNumber = scanner.nextLine();
        if (!typeAsNumber.equals("1") && !typeAsNumber.equals("2")) {
            System.out.println("Неверный тип категории");
            return;
        }
        Category.Type type = typeAsNumber.equals("1") ? Category.Type.INCOME : Category.Type.EXPENSE;
        Command command = new CreateCategoryCommand(categoryFacade, name, type);
        command.execute();
    }

    public static void createOperation(OperationFacade operationFacade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID счёта: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID счёта");
            scanner.nextLine();
        }
        Long accountId = scanner.nextLong();
        scanner.nextLine();
        if (!bankAccountFacade.checkBankAccount(accountId)) {
            System.out.println("Счёт с таким ID не найден");
            return;
        }
        System.out.printf("""
                Выбранный счёт:
                ID: %d, Название: %s, Баланс: %.2f
                """, accountId, bankAccountFacade.getBankAccount(accountId).getName(), bankAccountFacade.getBankAccount(accountId).getBalance());

        System.out.print("Введите ID категории: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID категории");
            scanner.nextLine();
        }
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        if (!categoryFacade.checkCategory(categoryId)) {
            System.out.println("Категория с таким ID не найдена");
            return;
        }

        System.out.printf("""
                Выбранная категория:
                ID: %d, Название: %s, Тип: %s
                """, categoryId, categoryFacade.getCategory(categoryId).getName(), categoryFacade.getCategory(categoryId).getType());

        System.out.print("""
                Введите тип операции:
                1. INCOME
                2. EXPENSE
                """);
        String typeAsNumber = scanner.nextLine();
        if (!typeAsNumber.equals("1") && !typeAsNumber.equals("2")) {
            System.out.println("Неверный тип операции");
            return;
        }
        Operation.Type type = typeAsNumber.equals("1") ? Operation.Type.INCOME : Operation.Type.EXPENSE;

        Category category = categoryFacade.getCategory(categoryId);
        if (type == Operation.Type.INCOME && category.getType() == Category.Type.EXPENSE) {
            System.out.println("Нельзя создать операцию с типом доход, если категория - расход");
            return;
        }
        if (type == Operation.Type.EXPENSE && category.getType() == Category.Type.INCOME) {
            System.out.println("Нельзя создать операцию с типом расход, если категория - доход");
            return;
        }

        System.out.print("Введите сумму операции: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Неверный формат суммы операции");
            scanner.nextLine();
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Введите дату операции (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Введите время операции (HH:mm): ");
        String time = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(date + "T" + time);

        System.out.print("Введите описание операции: ");
        String description = scanner.nextLine();

        Command command = new CreateOperationCommand(operationFacade, type, accountId, amount, dateTime, description, categoryId);
        command.execute();
    }

    public static void viewAllAccounts(BankAccountFacade bankAccountFacade) {
        System.out.println("Все счета:");
        Command command = new ViewAllAccountsCommand(bankAccountFacade);
        command.execute();
    }

    public static void viewAllCategories(CategoryFacade categoryFacade) {
        System.out.println("Все категории:");
        Command command = new ViewAllCategoriesCommand(categoryFacade);
        command.execute();
    }

    public static void viewAllOperations(OperationFacade operationFacade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade) {
        System.out.println("Все операции:");
        Command command = new ViewAllOperationsCommand(operationFacade, categoryFacade, bankAccountFacade);
        command.execute();
    }

    public static void recalculateBalance(BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID счёта: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID счёта");
            scanner.nextLine();
        }
        Long accountId = scanner.nextLong();
        scanner.nextLine();
        if (!bankAccountFacade.checkBankAccount(accountId)) {
            System.out.println("Счёт с таким ID не найден");
            return;
        }

        System.out.println("Старый баланса счёта: " + bankAccountFacade.getBankAccount(accountId).getBalance());
        Command command = new TimingDecorator(new RecalculateBalanceCommand(bankAccountFacade, accountId));
        command.execute();
        System.out.println("Новый баланс счёта: " + bankAccountFacade.getBankAccount(accountId).getBalance());
    }

    public static void analitycs(AnalyticsFacade analyticsFacade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade) {
        System.out.println("1. Получить все доходы");
        System.out.println("2. Получить все расходы");
        System.out.println("3. Получить разницу по счёту за период");
        System.out.println("4. Получить операции по категории");
        System.out.println("0. Назад");
        System.out.print("Выберите действие: ");
        String action = scanner.nextLine();
        switch (action) {
            case "1" -> getIncomes(analyticsFacade);
            case "2" -> getExpenses(analyticsFacade);
            case "3" -> getAccountDifferenceForPeriod(analyticsFacade, bankAccountFacade);
            case "4" -> groupOperationsByCategory(analyticsFacade, categoryFacade, bankAccountFacade);
            case "0" -> System.out.println("Возврат в главное меню");
            default -> System.out.println("Неверное действие");
        }
    }

    public static void getIncomes(AnalyticsFacade analyticsFacade) {
        Command command = new TimingDecorator(new GetIncomesCommand(analyticsFacade));
        command.execute();
    }

    public static void getExpenses(AnalyticsFacade analyticsFacade) {
        Command command = new TimingDecorator(new GetExpensesCommand(analyticsFacade));
        command.execute();
    }

    public static void getAccountDifferenceForPeriod(AnalyticsFacade analyticsFacade, BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID счёта: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID счёта");
            scanner.nextLine();
        }
        Long accountId = scanner.nextLong();
        scanner.nextLine();
        if (!bankAccountFacade.checkBankAccount(accountId)) {
            System.out.println("Счёт с таким ID не найден");
            return;
        }
        System.out.print("Введите начальную дату (yyyy-MM-dd): ");
        String start = scanner.nextLine();
        LocalDateTime startDate;
        try {
            startDate = LocalDateTime.parse(start + "T00:00");
        } catch (Exception e) {
            System.out.println("Неверный формат даты");
            return;
        }

        System.out.print("Введите конечную дату (yyyy-MM-dd): ");
        String end = scanner.nextLine();
        LocalDateTime endDate;
        try {
            endDate = LocalDateTime.parse(end + "T00:00");
        } catch (Exception e) {
            System.out.println("Неверный формат даты");
            return;
        }

        Command command = new TimingDecorator(new GetAccountDifferenceForPeriodCommand(analyticsFacade, accountId, startDate, endDate));
        command.execute();
    }

    public static void groupOperationsByCategory(AnalyticsFacade analyticsFacade, CategoryFacade categoryFacade, BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID категории: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID категории");
            scanner.nextLine();
        }
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        if (!categoryFacade.checkCategory(categoryId)) {
            System.out.println("Категория с таким ID не найдена");
            return;
        }
        Command command = new TimingDecorator(new GetOperationsByCategoryCommand(analyticsFacade, categoryFacade, bankAccountFacade, categoryId));
        command.execute();
    }

    public static void editAccount(BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID счёта: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID счёта");
            scanner.nextLine();
        }
        Long accountId = scanner.nextLong();
        scanner.nextLine();
        if (!bankAccountFacade.checkBankAccount(accountId)) {
            System.out.println("Счёт с таким ID не найден");
            return;
        }

        BankAccount bankAccount = bankAccountFacade.getBankAccount(accountId);
        System.out.printf("""
                Текущий счёт:
                ID: %d, Название: %s, Баланс: %.2f
                """, bankAccount.getId(), bankAccount.getName(), bankAccount.getBalance());

        System.out.print("Введите новое название счёта: ");
        String name = scanner.nextLine();

        System.out.print("Введите новый баланс счёта(Пример: 100): ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Неверный формат баланса");
            scanner.nextLine();
        }
        double balance = scanner.nextDouble();

        Command command = new EditAccountCommand(bankAccountFacade, accountId, name, balance);
        command.execute();
    }

    public static void editCategory(CategoryFacade categoryFacade) {
        System.out.print("Введите ID категории: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID категории");
            scanner.nextLine();
        }
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        if (!categoryFacade.checkCategory(categoryId)) {
            System.out.println("Категория с таким ID не найдена");
            return;
        }

        Category category = categoryFacade.getCategory(categoryId);
        System.out.printf("""
                Текущая категория:
                ID: %d, Название: %s, Тип: %s
                """, category.getId(), category.getName(), category.getType());

        System.out.print("Введите новое название категории: ");
        String name = scanner.nextLine();

        Command command = new EditCategoryCommand(categoryFacade, categoryId, name);
        command.execute();
    }

    public static void editOperation(OperationFacade operationFacade, CategoryFacade categoryFacade) {
        System.out.print("Введите ID операции: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID операции");
            scanner.nextLine();
        }
        Long operationId = scanner.nextLong();
        scanner.nextLine();
        if (!operationFacade.checkOperation(operationId)) {
            System.out.println("Операция с таким ID не найдена");
            return;
        }

        Operation operation = operationFacade.getOperation(operationId);
        System.out.printf("""
                Текущая операция:
                ID: %d, Тип: %s, Счёт: %d, Сумма: %.2f, Дата: %s, Описание: %s, Категория: %d
                """, operation.getId(), operation.getType(), operation.getBankAccountId(), operation.getAmount(), operation.getDateTime(), operation.getDescription(), operation.getCategoryId());

        System.out.print("""
                Введите новый тип операции:
                1. INCOME
                2. EXPENSE
                """);

        String typeAsNumber = scanner.nextLine();
        if (!typeAsNumber.equals("1") && !typeAsNumber.equals("2")) {
            System.out.println("Неверный тип операции");
            return;
        }
        Operation.Type type = typeAsNumber.equals("1") ? Operation.Type.INCOME : Operation.Type.EXPENSE;

        Category category = categoryFacade.getCategory(operation.getCategoryId());
        if (type == Operation.Type.INCOME && category.getType() == Category.Type.EXPENSE) {
            System.out.println("Нельзя изменить тип операции на доход, если категория - расход");
            return;
        }

        if (type == Operation.Type.EXPENSE && category.getType() == Category.Type.INCOME) {
            System.out.println("Нельзя изменить тип операции на расход, если категория - доход");
            return;
        }

        System.out.print("Введите новую сумму операции: ");
        while (!scanner.hasNextDouble()) {
            System.out.println("Неверный формат суммы операции");
            scanner.nextLine();
        }
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Введите новую дату операции (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Введите новое время операции (HH:mm): ");
        String time = scanner.nextLine();
        LocalDateTime dateTime;
        try {
            dateTime = LocalDateTime.parse(date + "T" + time);
        } catch (Exception e) {
            System.out.println("Неверный формат даты и времени");
            return;
        }

        System.out.print("Введите новое описание операции: ");
        String description = scanner.nextLine();

        Command command = new EditOperationCommand(operationFacade, operationId, type, amount, dateTime, description);
        command.execute();
    }

    public static void deleteAccount(BankAccountFacade bankAccountFacade) {
        System.out.print("Введите ID счёта: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID счёта");
            scanner.nextLine();
        }
        Long accountId = scanner.nextLong();
        scanner.nextLine();
        if (!bankAccountFacade.checkBankAccount(accountId)) {
            System.out.println("Счёт с таким ID не найден");
            return;
        }
        Command command = new DeleteAccountCommand(bankAccountFacade, accountId);
        command.execute();
    }

    public static void deleteCategory(CategoryFacade categoryFacade, OperationFacade operationFacade) {
        System.out.print("Введите ID категории: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID категории");
            scanner.nextLine();
        }
        Long categoryId = scanner.nextLong();
        scanner.nextLine();
        if (!categoryFacade.checkCategory(categoryId)) {
            System.out.println("Категория с таким ID не найдена");
            return;
        }

        List<Operation> operations = operationFacade.getAllOperations();
        for (Operation operation : operations) {
            if (operation.getCategoryId().equals(categoryId)) {
                System.out.println("Нельзя удалить категорию, так как она используется в операциях");
                return;
            }
        }

        Command command = new DeleteCategoryCommand(categoryFacade, categoryId);
        command.execute();
    }

    public static void deleteOperation(OperationFacade operationFacade) {
        System.out.print("Введите ID операции: ");
        while (!scanner.hasNextLong()) {
            System.out.println("Неверный формат ID операции");
            scanner.nextLine();
        }
        Long operationId = scanner.nextLong();
        scanner.nextLine();
        if (!operationFacade.checkOperation(operationId)) {
            System.out.println("Операция с таким ID не найдена");
            return;
        }
        Command command = new DeleteOperationCommand(operationFacade, operationId);
        command.execute();
    }

    public static void importFile(List<Importer<BankAccount>> accountImporters, List<Importer<Category>> categoryImporters, List<Importer<Operation>> operationImporters) {
        System.out.print("Введите путь к файлу: ");
        String filePath = scanner.nextLine();
        System.out.print("Выберите тип данных (1 - BankAccount, 2 - Category, 3 - Operation): ");
        int type = scanner.nextInt();
        System.out.print("Выберите формат (1 - JSON, 2 - CSV, 3 - YAML): ");
        int format = scanner.nextInt();
        scanner.nextLine();

        try {
            File file = new File(filePath);
            if (type == 1) {
                accountImporters.get(format - 1).importData(file);
            } else if (type == 2) {
                categoryImporters.get(format - 1).importData(file);
            } else if (type == 3) {
                operationImporters.get(format - 1).importData(file);
            } else {
                System.out.println("Неверный тип данных");
            }
        } catch (Exception e) {
            System.out.println("Ошибка импорта: " + e.getMessage());
        }

    }
    public static void exportFile(List<ExporterVisitor> exporters) {
        System.out.print("Введите путь к файлу для экспорта: ");
        String filePath = scanner.nextLine();
        System.out.print("Выберите тип данных (1 - BankAccount, 2 - Category, 3 - Operation): ");
        int type = scanner.nextInt();
        System.out.print("Выберите формат (1 - JSON, 2 - CSV, 3 - YAML): ");
        int format = scanner.nextInt();
        System.out.print("Введите ID объекта: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            String data;
            if (type == 1) {
                data = exporters.get(format - 1).visit(new BankAccount(id, "", 0));
            } else if (type == 2) {
                data = exporters.get(format - 1).visit(new Category(id, Category.Type.INCOME, ""));
            } else if (type == 3) {
                data = exporters.get(format - 1).visit(new Operation(id, Operation.Type.INCOME, 0L, 0.0, LocalDateTime.now(), "", 0L));
            } else {
                System.out.println("Неверный тип данных");
                return;
            }
            fileWriter.write(data);
            System.out.println("Данные успешно экспортированы");
        } catch (Exception e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}
