package dev.codescreen.controller;

import dev.codescreen.dto.AuthorizationRequest;
import dev.codescreen.dto.AuthorizationResponse;
import dev.codescreen.dto.LoadRequest;
import dev.codescreen.dto.LoadResponse;
import dev.codescreen.model.User;
import dev.codescreen.model.UserAccount;
import dev.codescreen.model.UserDetails;
import dev.codescreen.service.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserAccountControllerTest {

    @InjectMocks
    UserAccountController userAccountController;

    @Mock
    UserAccountService userAccountService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPing() {
        ResponseEntity<String> response = userAccountController.ping();
        assertTrue(Objects.requireNonNull(response.getBody()).startsWith("Pong! Current server time is:"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAuthorizeTransactionInsufficientBalance() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);
        UserDetails userDetails = new UserDetails(
            "User1", "LastName", "user1@example.com", "1234567890"
        );
        User user = new User(userDetails);
        UserAccount userAccount = new UserAccount(user);
        when(userAccountService.getUserAccount(request.getUserId())).thenReturn(userAccount);

        ResponseEntity<AuthorizationResponse> response = userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        // verify that the transaction was declined
        assertEquals("DECLINED", response.getBody().getResponseCode());
    }

    @Test
    public void testLoadTransaction() {
        LoadRequest request = new LoadRequest(1, "messageId", 100.0);
        UserDetails userDetails = new UserDetails(
            "User1", "LastName", "user1@example.com", "1234567890"
        );
        User user = new User(userDetails);
        UserAccount userAccount = new UserAccount(user);
        when(userAccountService.getUserAccount(request.getUserId())).thenReturn(userAccount);

        ResponseEntity<LoadResponse> response = userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        // verify that the balance was updated correctly
        assertEquals(100.0, response.getBody().getBalance(), 0.001);
    }
}