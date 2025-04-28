package models;

import java.sql.Timestamp;

public class Transaction {
    private int id;
    private String accountNumber;
    private String description;
    private double amount;
    private String type;
    private Timestamp transactionDate;

    public Transaction(int id, String accountNumber, String description, 
                      double amount, String type, Timestamp transactionDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
    }

    // Getters
    public int getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public Timestamp getTransactionDate() { return transactionDate; }
    
    public String getFormattedDate() {
        return transactionDate.toString().substring(0, 10);
    }
}