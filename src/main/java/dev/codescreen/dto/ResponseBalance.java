package dev.codescreen.dto;

import dev.codescreen.model.TransactionType;

/*
* This class represents the balance of a user account. It is used in the Response classes.
* A balance has a double amount, a String currency, and a TransactionType of either DEBIT or CREDIT.
* */
public class ResponseBalance {
    private double amount;
    private String currency;
    private TransactionType debitOrCredit;

    public ResponseBalance(double amount, String currency, TransactionType debitOrCredit) {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    // GETTERS ------------------------------------------------
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public TransactionType getDebitOrCredit() {
        return debitOrCredit;
    }

    // SETTERS ------------------------------------------------
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDebitOrCredit(TransactionType debitOrCredit) {
        this.debitOrCredit = debitOrCredit;
    }
}
