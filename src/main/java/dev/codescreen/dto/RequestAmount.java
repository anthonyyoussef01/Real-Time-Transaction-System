package dev.codescreen.dto;

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
