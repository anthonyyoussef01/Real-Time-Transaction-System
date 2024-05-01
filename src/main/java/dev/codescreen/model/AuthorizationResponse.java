package dev.codescreen.model;

public class AuthorizationResponse {
    private Integer userId;
    private String messageId;
    private String responseCode;
    private double balance;

    public AuthorizationResponse(Integer userId, String messageId, String responseCode, double balance) {
        this.userId = userId;
        this.messageId = messageId;
        this.responseCode = responseCode;
        this.balance = balance;
    }

    // GETTERS ------------------------------------------------\
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getResponseCode() {
        return responseCode;
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

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
