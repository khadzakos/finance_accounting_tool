package hse.accounting.domain;
/**
 * BankAccount - класс, описывающий банковский счет.
*/
public class BankAccount {
    private final Long id;
    private String name;
    private double balance;

    public BankAccount(Long id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getBalance() { return balance; }

    public void setName(String name) { this.name = name; }
    public void setBalance(double balance) { this.balance = balance; }
}