package dev.codescreen.model;

public class LoadResponse {
    private Integer userId;
    private String messageId;
    private double balance;

    public LoadResponse(Integer userId, String messageId, double balance) {
        this.userId = userId;
        this.messageId = messageId;
        this.balance = balance;
    }

    // GETTERS ------------------------------------------------\
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public double getBalance() {
        return balance;
    }

    // SETTERS ------------------------------------------------\
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
