package dev.codescreen.dto;

public class LoadRequest {
    private Integer userId;
    private String messageId;
    private RequestAmount transactionAmount;

    public LoadRequest(String userId, String messageId, RequestAmount transactionAmount) {
        this.userId = Integer.parseInt(userId);
        this.messageId = messageId;
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
