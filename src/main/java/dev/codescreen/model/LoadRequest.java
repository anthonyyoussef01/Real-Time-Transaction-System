package dev.codescreen.model;

public class LoadRequest {
    private Integer userId;
    private String messageId;
    private double amount;

    public LoadRequest(Integer userId, String messageId, double amount) {
        this.userId = userId;
        this.messageId = messageId;
        this.amount = (double) amount;
    }

    // GETTERS ------------------------------------------------\
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public double getAmount() {
        return amount;
    }

    // SETTERS ------------------------------------------------\
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
