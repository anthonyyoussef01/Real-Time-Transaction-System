package dev.codescreen.model;

import java.util.Objects;

/*
 * This class represents a TransactionEvent object.
 * A TransactionEvent object will have a long timestamp, TransactionType transaction type, and double amount.
 */
public class TransactionEvent {
    private long timestamp;
    private TransactionType transactionType;
    private double amount;
    private String messageId;
    private String currency;

    public TransactionEvent(long timestamp, TransactionType transactionType, double amount, String messageId, String currency) {
        this.timestamp = timestamp;
        this.transactionType = transactionType;
        this.amount = amount;
        this.messageId = messageId;
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TransactionEvent that = (TransactionEvent) o;
        return this.timestamp == that.timestamp &&
               Objects.equals(this.transactionType, that.transactionType) &&    // Use Objects.equals() for null safety
               Double.compare(that.amount, this.amount) == 0;
    }

    // GETTERS ------------------------------------------------
    public long getTimestamp() {
        return timestamp;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    // SETTERS ------------------------------------------------
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
