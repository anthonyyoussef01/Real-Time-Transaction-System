package dev.codescreen.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the Ping DTO class.
 * Returns the server time, a Date-Time string.
 */
public class Ping {
    private final String serverTime;

    public Ping() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss.SSS");
        this.serverTime = LocalDateTime.now().format(formatter);
    }

    public String getServerTime() {
        return serverTime;
    }
}
