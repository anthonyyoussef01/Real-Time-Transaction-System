package dev.codescreen.controller;

import dev.codescreen.dto.*;
import dev.codescreen.model.*;
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
    private UserDetails userDetails;
    private User user;
    private UserAccount userAccount;
    private TransactionEvent creditEventOneFiftyOneLong;
    private TransactionEvent debitEventFiftyTwoLong;
    private TransactionEvent creditEventOneHundredCurrentTime;
    private TransactionEvent debitEventOneHundredCurrentTime;
    

    @InjectMocks
    UserAccountController userAccountController;

    @Mock
    UserAccountService userAccountService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.userDetails = new UserDetails(
            "User1", "LastName", "user1@example.com", "1234567890"
        );
        this.user = new User(this.userDetails);
        this.userAccount = new UserAccount(this.user);
        this.creditEventOneFiftyOneLong = new TransactionEvent(1L, TransactionType.CREDIT, 150.0);
        this.debitEventFiftyTwoLong = new TransactionEvent(2L, TransactionType.DEBIT, 50.0);
        this.creditEventOneHundredCurrentTime = 
            new TransactionEvent(System.currentTimeMillis(), TransactionType.CREDIT, 100.0);
        this.debitEventOneHundredCurrentTime =
            new TransactionEvent(System.currentTimeMillis(), TransactionType.DEBIT, 100.0);
    }

    // testPing() test ------------------------------------------------
    @Test
    public void testPing() {
        ResponseEntity<String> response = this.userAccountController.ping();
        assertTrue(Objects.requireNonNull(response.getBody()).startsWith("Pong! Current server time is:"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // testAuthorizeTransaction() tests ------------------------------------------------
    @Test
    public void testAuthorizeTransactionInsufficientBalance() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());

        assertEquals("DECLINED", response.getBody().getResponseCode()); // verify transaction was declined
    }

    @Test
    public void testAuthorizeTransactionInsufficientBalanceNonZero() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 150.01);

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("DECLINED", Objects.requireNonNull(response.getBody()).getResponseCode());
    }

    @Test
    public void testAuthorizeTransactionUserNotFound() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(null);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAuthorizeTransactionApproved() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals("APPROVED", response.getBody().getResponseCode()); // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(50.0, response.getBody().getBalance(), 0.001);
        assertEquals(user.getAccountBalance(), 50.0, 0.001);
    }

    @Test
    public void testAuthorizeTransactionIncorrectBalance() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user.setAccountBalance(50.0);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals("APPROVED", response.getBody().getResponseCode());      // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(50.0, response.getBody().getBalance(), 0.001);
        assertEquals(user.getAccountBalance(), 50.0, 0.001);
    }

    @Test
    public void testAuthorizeTransactionZeroAmount() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 0.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals("APPROVED", response.getBody().getResponseCode());      // verify transaction was approved
        assertEquals(
            user.getAccountBalance(), response.getBody().getBalance(), 0.001          // verify balance was not changed
        );
    }

    @Test
    public void testAuthorizeTransactionNegativeAmount() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", -100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals("DECLINED", response.getBody().getResponseCode());      // verify transaction was declined
        assertEquals(
            user.getAccountBalance(), response.getBody().getBalance(), 0.001          // verify balance was not changed
        );
    }

    @Test
    public void testAuthorizeTransactionExactBalance() {
        AuthorizationRequest request = new AuthorizationRequest(1, "messageId", 100.0);
        
        try {
            userAccount.addTransactionEvent(this.creditEventOneHundredCurrentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(userAccount);
    
        ResponseEntity<AuthorizationResponse> response = this.userAccountController.authorizeTransaction(request);
    
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals("APPROVED", response.getBody().getResponseCode());      // verify transaction was approved
        assertEquals(0.0, response.getBody().getBalance(), 0.001);          // verify balance is now zero
    }

    // testLoadTransaction() tests ------------------------------------------------
    @Test
    public void testLoadTransaction() {
        LoadRequest request = new LoadRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals(
            100.0,
            response.getBody().getBalance(), 0.001                              // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionPositiveAmount() {
        LoadRequest request = new LoadRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals(
            100.0, response.getBody().getBalance(), 0.001               // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionZeroAmount() {
        LoadRequest request = new LoadRequest(1, "messageId", 0.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals(0.0, response.getBody().getBalance(), 0.001);      // verify balance was not changed
    }

    @Test
    public void testLoadTransactionUserNotFound() {
        LoadRequest request = new LoadRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(null);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoadTransactionNegativeAmount() {
        LoadRequest request = new LoadRequest(1, "messageId", -100.0);

        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(response.getBody()).getUserId());
        assertEquals(request.getMessageId(), response.getBody().getMessageId());
        assertEquals(0.0, response.getBody().getBalance(), 0.001);      // verify balance was not changed
    }

    @Test
    public void testLoadTransactionLargeAmount() {
        LoadRequest request = new LoadRequest(1, "messageId", Double.MAX_VALUE);
        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Double.MAX_VALUE, Objects.requireNonNull(response.getBody()).getBalance(), 0.001);
    }

    @Test
    public void testLoadTransactionSmallAmount() {
        LoadRequest request = new LoadRequest(1, "messageId", Double.MIN_VALUE);
        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> response = this.userAccountController.loadTransaction(request);

        // Add assertions to verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Double.MIN_VALUE, Objects.requireNonNull(response.getBody()).getBalance(), 0.001);
    }

    // I think this works, but I'm not sure.
    @Test
    public void testConcurrentLoadTransactions() throws InterruptedException {
        LoadRequest request = new LoadRequest(1, "messageId", 100.0);
        when(this.userAccountService.getUserAccount(request.getUserId())).thenReturn(this.userAccount);

        Thread thread1 = new Thread(() -> this.userAccountController.loadTransaction(request));
        Thread thread2 = new Thread(() -> this.userAccountController.loadTransaction(request));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(200.0, this.userAccount.recalculateBalance(), 0.001);
    }

    // loadTransaction() and authorizeTransaction() tests ------------------------------------------------
    @Test
    public void testLoadTransactionAndAuthorizeTransaction() {
        LoadRequest loadRequest = new LoadRequest(1, "messageId", 100.0);
        AuthorizationRequest authorizeRequest = new AuthorizationRequest(1, "messageId", 100.0);

        when(this.userAccountService.getUserAccount(loadRequest.getUserId())).thenReturn(this.userAccount);
        when(this.userAccountService.getUserAccount(authorizeRequest.getUserId())).thenReturn(this.userAccount);

        ResponseEntity<LoadResponse> loadResponse = this.userAccountController.loadTransaction(loadRequest);
        ResponseEntity<AuthorizationResponse> authorizeResponse = this.userAccountController.authorizeTransaction(authorizeRequest);

        assertEquals(HttpStatus.CREATED, loadResponse.getStatusCode());
        assertEquals(HttpStatus.CREATED, authorizeResponse.getStatusCode());
        assertEquals(loadRequest.getUserId(), Objects.requireNonNull(loadResponse.getBody()).getUserId());
        assertEquals(loadRequest.getMessageId(), loadResponse.getBody().getMessageId());
        assertEquals(authorizeRequest.getUserId(), Objects.requireNonNull(authorizeResponse.getBody()).getUserId());
        assertEquals(authorizeRequest.getMessageId(), authorizeResponse.getBody().getMessageId());
        assertEquals(
            100.0, loadResponse.getBody().getBalance(), 0.001               // verify balance was updated correctly
        );
        assertEquals(
            0.0, authorizeResponse.getBody().getBalance(), 0.001           // verify balance was updated correctly
        );
    }
}