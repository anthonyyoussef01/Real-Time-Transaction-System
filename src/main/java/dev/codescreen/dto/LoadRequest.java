package dev.codescreen.dto;

/*
* Represents the request to load money into a User's account.
* A LoadRequest includes an Integer user ID, a String message ID, and a RequestAmount transaction amount.
* */
public class LoadRequest {
    private Integer userId;
    private String messageId;
    private RequestAmount transactionAmount;

    public LoadRequest(String userId, String messageId, RequestAmount transactionAmount) {
        this.messageId = messageId;
        this.userId = Integer.parseInt(userId);
        this.transactionAmount = transactionAmount;
    }

    // GETTERS ------------------------------------------------
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public RequestAmount getTransactionAmount() {
        return transactionAmount;
    }
}
