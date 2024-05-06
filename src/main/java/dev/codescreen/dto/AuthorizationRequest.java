package dev.codescreen.dto;

public class AuthorizationRequest {
    private Integer userId;
    private String messageId;
    private RequestAmount transactionAmount;

    public AuthorizationRequest(String userId, String messageId, RequestAmount transactionAmount) {
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
