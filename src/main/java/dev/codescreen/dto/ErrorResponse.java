package dev.codescreen.dto;

public class ErrorResponse {
    private final String message;
    private final String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    // GETTERS ------------------------------------------------
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
