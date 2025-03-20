package hse.accounting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BankAccount - класс, описывающий банковский счет.
*/
public class BankAccount {
    @JsonProperty("accountId")
    private final Long id;
    @JsonProperty("accountName")
    private String name;
    @JsonProperty("balance")
    private double balance;
    private double currentBalance;

    public BankAccount(Long id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currentBalance = balance;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public double getBalance() { return currentBalance; }
    public double getStartBalance() { return balance; }

    public void setName(String name) { this.name = name; }
    public void setBalance(double balance) { this.currentBalance = balance; }
    public void setBalances(double balance) {
        this.balance = balance;
        this.currentBalance = balance;
    }
}