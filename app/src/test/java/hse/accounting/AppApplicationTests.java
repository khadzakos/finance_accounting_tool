package hse.accounting;

import static org.junit.jupiter.api.Assertions.*;

import hse.accounting.config.ApplicationConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hse.accounting.domain.*;
import hse.accounting.facade.*;
import hse.accounting.command.*;
import hse.accounting.repository.*;
import hse.accounting.factory.*;
import hse.accounting.idgenerator.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApplicationConfig.class)
class AppApplicationTests {

    private BankAccountFacade bankAccountFacade;
    private CategoryFacade categoryFacade;
    private OperationFacade operationFacade;
    private InMemoryRepository<BankAccount> bankAccountRepository;
    private InMemoryRepository<Category> categoryRepository;
    private InMemoryRepository<Operation> operationRepository;
    private IdGenerator idGenerator;
    private BankAccountFactory bankAccountFactory;
    private CategoryFactory categoryFactory;
    private OperationFactory operationFactory;

    @BeforeEach
    void setUp() {
        bankAccountRepository = new InMemoryRepository<>();
        categoryRepository = new InMemoryRepository<>();
        operationRepository = new InMemoryRepository<>();
        
        idGenerator = new IdGenerator();
        idGenerator.setStrategy(new IncrementalIStrategy());
        
        operationFactory = new OperationFactory();
        bankAccountFactory = new BankAccountFactory();
        categoryFactory = new CategoryFactory();
        
        operationFacade = new OperationFacade(operationRepository, operationFactory);
        bankAccountFacade = new BankAccountFacade(bankAccountRepository, bankAccountFactory, operationFacade);
        categoryFacade = new CategoryFacade(categoryRepository, categoryFactory);
    }

    @Test
    void testCreateBankAccount() {
        CreateAccountCommand command = new CreateAccountCommand(bankAccountFacade, "Savings", 1000);
        command.execute();
        List<BankAccount> accounts = bankAccountFacade.getAllBankAccounts();
        assertEquals(1, accounts.size());
        assertEquals("Savings", accounts.get(0).getName());
        assertEquals(1000, accounts.get(0).getBalance());
    }

    @Test
    void testCreateMultipleBankAccounts() {
        bankAccountFacade.createBankAccount("Savings", 1000);
        bankAccountFacade.createBankAccount("Checking", 2000);
        bankAccountFacade.createBankAccount("Investment", 5000);

        List<BankAccount> accounts = bankAccountFacade.getAllBankAccounts();
        assertEquals(3, accounts.size());
        assertEquals(8000, accounts.stream().mapToDouble(BankAccount::getBalance).sum());
    }

    @Test
    void testDeleteBankAccount() {
        bankAccountFacade.createBankAccount("ToDelete", 1000);
        Long accountId = bankAccountFacade.getAllBankAccounts().get(0).getId();

        DeleteAccountCommand command = new DeleteAccountCommand(bankAccountFacade, accountId);
        command.execute();

        assertTrue(bankAccountFacade.getAllBankAccounts().isEmpty());
    }

    // Тесты для категорий
    @Test
    void testCreateCategory() {
        CreateCategoryCommand command = new CreateCategoryCommand(categoryFacade, "Food", Category.Type.EXPENSE);
        command.execute();
        List<Category> categories = categoryFacade.getAllCategories();
        assertEquals(1, categories.size());
        assertEquals("Food", categories.get(0).getName());
        assertEquals(Category.Type.EXPENSE, categories.get(0).getType());
    }

    @Test
    void testCreateMultipleCategories() {
        categoryFacade.createCategory("Food", Category.Type.EXPENSE);
        categoryFacade.createCategory("Salary", Category.Type.INCOME);
        categoryFacade.createCategory("Transport", Category.Type.EXPENSE);

        List<Category> categories = categoryFacade.getAllCategories();
        assertEquals(3, categories.size());
        assertEquals(2, categories.stream()
                .filter(c -> c.getType() == Category.Type.EXPENSE)
                .count());
    }

    @Test
    void testDeleteCategoryWithNoOperations() {
        categoryFacade.createCategory("Entertainment", Category.Type.EXPENSE);
        Long categoryId = categoryFacade.getAllCategories().get(0).getId();

        DeleteCategoryCommand command = new DeleteCategoryCommand(categoryFacade, categoryId);
        command.execute();

        assertTrue(categoryFacade.getAllCategories().isEmpty());
    }

    // Тесты для операций
    @Test
    void testCreateExpenseOperation() {
        bankAccountFacade.createBankAccount("Wallet", 500);
        categoryFacade.createCategory("Transport", Category.Type.EXPENSE);
        LocalDateTime operationDate = LocalDateTime.now();

        CreateOperationCommand command = new CreateOperationCommand(
            operationFacade,
            Operation.Type.EXPENSE,
            1L,
            50,
            operationDate,
            "Bus ticket",
            1L
        );
        command.execute();

        List<Operation> operations = operationFacade.getAllOperations();
        assertEquals(1, operations.size());
        assertEquals(50, operations.get(0).getAmount());
        assertEquals(Operation.Type.EXPENSE, operations.get(0).getType());
        assertEquals("Bus ticket", operations.get(0).getDescription());
    }

    @Test
    void testCreateIncomeOperation() {
        bankAccountFacade.createBankAccount("Salary Account", 0);
        categoryFacade.createCategory("Salary", Category.Type.INCOME);
        LocalDateTime operationDate = LocalDateTime.now();

        CreateOperationCommand command = new CreateOperationCommand(
            operationFacade,
            Operation.Type.INCOME,
            1L,
            5000,
            operationDate,
            "Monthly salary",
            1L
        );
        command.execute();

        List<Operation> operations = operationFacade.getAllOperations();
        assertEquals(1, operations.size());
        assertEquals(5000, operations.get(0).getAmount());
        assertEquals(Operation.Type.INCOME, operations.get(0).getType());
    }

    @Test
    void testEditOperation() {
        bankAccountFacade.createBankAccount("Credit Card", 2000);
        categoryFacade.createCategory("Shopping", Category.Type.EXPENSE);
        LocalDateTime initialDate = LocalDateTime.parse("2024-01-01T10:00");
        LocalDateTime newDate = LocalDateTime.parse("2024-01-01T12:00");

        CreateOperationCommand createCommand = new CreateOperationCommand(
            operationFacade,
            Operation.Type.EXPENSE,
            1L,
            200,
            initialDate,
            "Bought shoes",
            1L
        );
        createCommand.execute();

        List<Operation> operations = operationFacade.getAllOperations();
        assertEquals(1, operations.size());
        Operation originalOperation = operations.get(0);

        EditOperationCommand editCommand = new EditOperationCommand(
            operationFacade,
            originalOperation.getId(),
            Operation.Type.EXPENSE,
            150,
            newDate,
            "Bought sneakers"
        );
        editCommand.execute();

        Operation updatedOperation = operationFacade.getOperation(originalOperation.getId());
        assertNotNull(updatedOperation, "Updated operation should not be null");
        assertEquals(150, updatedOperation.getAmount());
        assertEquals("Bought sneakers", updatedOperation.getDescription());
        assertEquals(newDate, updatedOperation.getDateTime());
    }

//    @Test
//    void testDeleteOperation() {
//        bankAccountFacade.createBankAccount("Test Account", 1000);
//        categoryFacade.createCategory("Test Category", Category.Type.EXPENSE);
//
//        operationFacade.createOperation(
//            Operation.Type.EXPENSE,
//            0L,
//            100,
//            LocalDateTime.now(),
//            "Test operation",
//            1L
//        );
//
//        DeleteOperationCommand command = new DeleteOperationCommand(operationFacade, 1L);
//        command.execute();
//
//        assertTrue(operationFacade.getAllOperations().isEmpty());
//
//        BankAccount account = bankAccountFacade.getBankAccount(0L);
//        assertNotNull(account, "Bank account should exist");
//        assertEquals(900, account.getBalance(), "Balance should be updated after operation deletion");
//    }
//
//    // Тесты для пересчета баланса
//    @Test
//    void testRecalculateBalance() {
//        bankAccountFacade.createBankAccount("Investment", 5000);
//        categoryFacade.createCategory("Investment", Category.Type.EXPENSE);
//        LocalDateTime now = LocalDateTime.now();
//
//        operationFacade.createOperation(
//            Operation.Type.EXPENSE,
//            1L,
//            500,
//            now,
//            "Stocks purchase",
//            1L
//        );
//
//        operationFacade.createOperation(
//            Operation.Type.EXPENSE,
//            0L,
//            300,
//            now.plusHours(1),
//            "More stocks",
//            1L
//        );
//
//        RecalculateBalanceCommand command = new RecalculateBalanceCommand(bankAccountFacade, 0L);
//        command.execute();
//
//        BankAccount updatedAccount = bankAccountFacade.getBankAccount(1L);
//        assertEquals(4200, updatedAccount.getBalance());
//    }
//
//    @Test
//    void testComplexBalanceCalculation() {
//        bankAccountFacade.createBankAccount("Mixed Operations", 1000);
//        categoryFacade.createCategory("Expense", Category.Type.EXPENSE);
//        categoryFacade.createCategory("Income", Category.Type.INCOME);
//        LocalDateTime now = LocalDateTime.now();
//
//        // Создаем серию операций
//        operationFacade.createOperation(
//            Operation.Type.EXPENSE,
//            0L,
//            200,
//            now,
//            "Expense 1",
//            1L
//        );
//
//        operationFacade.createOperation(
//            Operation.Type.INCOME,
//            0L,
//            500,
//            now.plusHours(1),
//            "Income 1",
//            2L
//        );
//
//        operationFacade.createOperation(
//            Operation.Type.EXPENSE,
//            0L,
//            300,
//            now.plusHours(2),
//            "Expense 2",
//            1L
//        );
//
//        RecalculateBalanceCommand command = new RecalculateBalanceCommand(bankAccountFacade, 0L);
//        command.execute();
//
//        BankAccount updatedAccount = bankAccountFacade.getBankAccount(1L);
//        assertEquals(1000, updatedAccount.getBalance());
//    }

    // Тесты на проверку ограничений
    @Test
    void testNegativeAmountValidation() {
        bankAccountFacade.createBankAccount("Test", 1000);
        categoryFacade.createCategory("Test", Category.Type.EXPENSE);

        assertThrows(IllegalArgumentException.class, () -> {
            operationFacade.createOperation(
                Operation.Type.EXPENSE,
                0L,
                -100,
                LocalDateTime.now(),
                "Negative amount",
                1L
            );
        });
    }

    @Test
    void testZeroAmountValidation() {
        bankAccountFacade.createBankAccount("Test", 1000);
        categoryFacade.createCategory("Test", Category.Type.EXPENSE);

        assertThrows(IllegalArgumentException.class, () -> {
            operationFacade.createOperation(
                Operation.Type.EXPENSE,
                0L,
                0,
                LocalDateTime.now(),
                "Zero amount",
                1L
            );
        });
    }

//    @Test
//    void testNonExistentAccountOperation() {
//        categoryFacade.createCategory("Test", Category.Type.EXPENSE);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            operationFacade.createOperation(
//                Operation.Type.EXPENSE,
//                999L,
//                100,
//                LocalDateTime.now(),
//                "Invalid account",
//                1L
//            );
//        });
//    }
//
//    @Test
//    void testNonExistentCategoryOperation() {
//        bankAccountFacade.createBankAccount("Test", 1000);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            operationFacade.createOperation(
//                Operation.Type.EXPENSE,
//                0L,
//                100,
//                LocalDateTime.now(),
//                "Invalid category",
//                999L
//            );
//        });
//    }
}