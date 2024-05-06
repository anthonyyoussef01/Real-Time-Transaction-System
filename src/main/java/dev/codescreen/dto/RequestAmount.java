package dev.codescreen.dto;

/*
 * Represents the request to load money into a User's account.
 * A LoadRequest includes an Integer user ID, a String message ID, and a RequestAmount transaction amount.
 */
public class RequestAmount {
    private final double amount;
    private final String currency;
    private final String transactionType;

    public RequestAmount(String amount, String currency, String transactionType) {
        this.amount = Double.parseDouble(amount);
        this.currency = currency;
        this.transactionType = transactionType;
    }

    // GETTERS ------------------------------------------------
    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTransactionType() {
        return transactionType;
    }
}
