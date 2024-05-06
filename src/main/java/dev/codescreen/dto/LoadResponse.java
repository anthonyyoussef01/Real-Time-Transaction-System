package dev.codescreen.dto;

public class LoadResponse {
    private final Integer userId;
    private final String messageId;
    private final ResponseBalance responseBalance;

    public LoadResponse(Integer userId, String messageId, ResponseBalance responseBalance) {
        this.messageId = messageId;
        this.userId = userId;
        this.responseBalance = responseBalance;
    }

    // GETTERS ------------------------------------------------
    public Integer getUserId() {
        return userId;
    }

    public String getMessageId() {
        return messageId;
    }

    public ResponseBalance getBalance() {
        return responseBalance;
    }
}
