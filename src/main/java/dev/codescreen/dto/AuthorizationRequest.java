package dev.codescreen.dto;

/**
 * Represents the response to an authorization request.
 * An authorization response includes an Integer user ID, a String message ID, and a RequestAmount transaction amount.
 */
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
