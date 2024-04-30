package dev.codescreen.model;

/*
 * This class represents a TransactionEvent object.
 * A TransactionEvent object will have a long timestamp, TransactionType transaction type, and double amount.
 */
public class TransactionEvent {
    private long timestamp;
    private TransactionType transactionType;
    private double amount;

    public TransactionEvent(long timestamp, TransactionType transactionType, double amount) {
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    // GETTERS
    public long getTimestamp() {
        return timestamp;
    }
    public TransactionType getTransactionType() {
        return transactionType;
    }
    public double getAmount() {
        return amount;
    }

    // SETTERS
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
