package dev.codescreen.dto;

public class AuthorizationResponse {
    private final Integer userId;
    private final String messageId;
    private final String responseCode;
    private final ResponseBalance responseBalance;

    public AuthorizationResponse(Integer userId, String messageId, String responseCode, ResponseBalance responseBalance) {
        this.messageId = messageId;
        this.userId = userId;
        this.responseCode = responseCode;
        this.responseBalance = responseBalance;
    }

    // GETTERS ------------------------------------------------
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public ResponseBalance getBalance() {
        return responseBalance;
    }
}
