package dev.codescreen.controller;

import dev.codescreen.dto.*;
import dev.codescreen.model.*;
import dev.codescreen.repository.UserAccountRepositoryImpl;
import dev.codescreen.service.UserAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountControllerTest {
    private UserDetails userDetails;
    private User user;
    private UserAccount userAccount;
    private UserDetails userDetails2;
    private User user2;
    private UserAccount userAccount2;
    private TransactionEvent creditEventOneFiftyOneLong;
    private TransactionEvent debitEventFiftyTwoLong;
    private TransactionEvent creditEventOneHundredCurrentTime;
    private TransactionEvent debitEventOneHundredCurrentTime;
    private Random random;
    private int nonexistentUserId;


    @InjectMocks
    UserAccountController userAccountController;

    UserAccountRepositoryImpl userAccountRepository;

    UserAccountServiceImpl userAccountService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        this.userAccountRepository = new UserAccountRepositoryImpl();
        this.userAccountService = new UserAccountServiceImpl(userAccountRepository);
        this.userAccountController = new UserAccountController(userAccountService);

        this.userDetails = new UserDetails(
            "User1", "LastName", "user1@example.com", "1234567890"
        );
        this.user = new User(this.userDetails);
        this.userAccount = new UserAccount(this.user, "USD");
        this.userAccountRepository.saveUserAccount(this.userAccount);

        this.userDetails2 = new UserDetails(
            "User2", "LastName", "user2@example.com", "0987654321"
        );
        this.user2 = new User(this.userDetails2);
        this.userAccount2 = new UserAccount(this.user2, "USD");
        this.userAccountRepository.saveUserAccount(this.userAccount2);

        // this makes sure that nonexistentUserId is not the same as user.getUserId() or user2.getUserId()
        // later, if we have a lot of users, we could just go through the userAccountRepository and make sure
        // that nonexistentUserId is not in the list of userIds
        int minUserId = 1;
        int maxUserId = Integer.MAX_VALUE;
        random = new Random();
        nonexistentUserId = random.nextInt(maxUserId - minUserId + 1) + minUserId;
        while (nonexistentUserId == user.getUserId() || nonexistentUserId == user2.getUserId()) {
            nonexistentUserId = random.nextInt(maxUserId - minUserId + 1) + minUserId;
        }

        this.creditEventOneFiftyOneLong = new TransactionEvent(
            1L, TransactionType.CREDIT, 150.0, "2341", "USD"
        );
        this.debitEventFiftyTwoLong = new TransactionEvent(
            2L, TransactionType.DEBIT, 50.0, "2342", "EGP"
        );
        this.creditEventOneHundredCurrentTime = new TransactionEvent(
            System.currentTimeMillis(), TransactionType.CREDIT, 100.0, "2343", "EUR"
        );
        this.debitEventOneHundredCurrentTime = new TransactionEvent(
            System.currentTimeMillis(), TransactionType.DEBIT, 100.0, "2344", "GBP"
        );
    }

    // testPing() test ------------------------------------------------
    @Test
    public void testPing() {
        ResponseEntity<?> response = this.userAccountController.ping();

        assertTrue(response.getBody() instanceof Ping);

        Ping pingResponse = (Ping) response.getBody();

        assertNotNull(pingResponse);
        assertNotNull(pingResponse.getServerTime());
        assertTrue(pingResponse.getServerTime().matches("\\d{4}-\\d{2}-\\d{2}-\\d{2}:\\d{2}:\\d{2}\\.\\d{3}"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // testAuthorizeTransaction() tests ------------------------------------------------
    @Test
    public void testAuthorizeTransactionInsufficientBalance() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(request.getMessageId(), responseBody.getMessage());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
    }

    @Test
    public void testAuthorizeTransactionInsufficientBalanceNonZero() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "150.01", "USD", "DEBIT"
            )
        );

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(request.getMessageId(), responseBody.getMessage());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
    }

    @Test
    public void testAuthorizeTransactionUserNotFound() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.nonexistentUserId), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
    }

    @Test
    public void testAuthorizeTransactionApproved() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof AuthorizationResponse);
        AuthorizationResponse responseBody = (AuthorizationResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(responseBody.getUserId()));
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals("APPROVED", responseBody.getResponseCode()); // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(user.getAccountBalance(), 50.0, 0.001);
    }

    @Test
    public void testAuthorizeTransactionIncorrectBalance() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        try {
            this.userAccount.addTransactionEvent(this.creditEventOneFiftyOneLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user.setAccountBalance(50.0);

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof AuthorizationResponse);
        AuthorizationResponse responseBody = (AuthorizationResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(responseBody.getUserId()));
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals("APPROVED", responseBody.getResponseCode()); // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(user.getAccountBalance(), 50.0, 0.001);
    }

    @Test
    public void testAuthorizeTransactionZeroAmount() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user2.getUserId()), "messageId", new RequestAmount(
                "0", "USD", "DEBIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof AuthorizationResponse);
        AuthorizationResponse responseBody = (AuthorizationResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(responseBody.getUserId()));
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals("APPROVED", responseBody.getResponseCode()); // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(user2.getAccountBalance(), 0, 0.001);
    }

    @Test
    public void testAuthorizeTransactionNegativeAmount() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "-100.0", "USD", "DEBIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
        // verify balance was updated correctly
        assertEquals(0, user.getAccountBalance(), 0.001);
    }

    @Test
    public void testAuthorizeTransactionExactBalance() {
        AuthorizationRequest request = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        try {
            userAccount.addTransactionEvent(this.creditEventOneHundredCurrentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<?> response = this.userAccountController.authorizeTransaction(request);
        assertTrue(response.getBody() instanceof AuthorizationResponse);
        AuthorizationResponse responseBody = (AuthorizationResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), Objects.requireNonNull(responseBody.getUserId()));
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals("APPROVED", responseBody.getResponseCode()); // verify transaction was approved
        // verify balance was updated correctly
        assertEquals(user.getAccountBalance(), 0, 0.001);
    }

    // testLoadTransaction() tests ------------------------------------------------
    @Test
    public void testLoadTransaction() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof LoadResponse);
        LoadResponse responseBody = (LoadResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), responseBody.getUserId());
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals(
            100.0,
            user.getAccountBalance(),
            0.001                              // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionDecimalAmount() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.45", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof LoadResponse);
        LoadResponse responseBody = (LoadResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), responseBody.getUserId());
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals(
            100.45,
            user.getAccountBalance(),
            0.001                              // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionZeroAmount() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "0", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof LoadResponse);
        LoadResponse responseBody = (LoadResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), responseBody.getUserId());
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals(
            0,
            user.getAccountBalance(),
            0.001                              // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionUserNotFound() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.nonexistentUserId), "messageId", new RequestAmount(
                "0", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(request.getMessageId(), responseBody.getMessage());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
    }

    @Test
    public void testLoadTransactionNegativeAmount() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "-100.0", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof ErrorResponse);
        ErrorResponse responseBody = (ErrorResponse) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("DECLINED", Objects.requireNonNull(responseBody.getCode())); //verify transaction declined
    }

    @Test
    public void testLoadTransactionLargeAmount() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                Double.toString(Double.MAX_VALUE), "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof LoadResponse);
        LoadResponse responseBody = (LoadResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), responseBody.getUserId());
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals(
            Double.MAX_VALUE,
            user.getAccountBalance(),
            0.001                              // verify balance was updated correctly
        );
    }

    @Test
    public void testLoadTransactionSmallAmount() {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                Double.toString(Double.MIN_VALUE), "USD", "CREDIT"
            )
        );

        ResponseEntity<?> response = this.userAccountController.loadTransaction(request);
        assertTrue(response.getBody() instanceof LoadResponse);
        LoadResponse responseBody = (LoadResponse) response.getBody();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(request.getUserId(), responseBody.getUserId());
        assertEquals(request.getMessageId(), responseBody.getMessageId());
        assertEquals(
            Double.MIN_VALUE,
            user.getAccountBalance(),
            0.001                              // verify balance was updated correctly
        );
    }

    // I believe this works (but both are loading anyways)
    @Test
    public void testConcurrentLoadTransactions() throws InterruptedException {
        LoadRequest request = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "CREDIT"
            )
        );

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
        LoadRequest loadRequest = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "CREDIT"
            )
        );
        AuthorizationRequest authorizeRequest = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                "100.0", "USD", "DEBIT"
            )
        );

        ResponseEntity<?> loadResponse = this.userAccountController.loadTransaction(loadRequest);
        assertTrue(loadResponse.getBody() instanceof LoadResponse);
        LoadResponse loadResponseBody = (LoadResponse) loadResponse.getBody();

        ResponseEntity<?> authorizeResponse = this.userAccountController.authorizeTransaction(authorizeRequest);
        assertTrue(authorizeResponse.getBody() instanceof AuthorizationResponse);
        AuthorizationResponse authorizeResponseBody = (AuthorizationResponse) authorizeResponse.getBody();

        assertEquals(HttpStatus.CREATED, loadResponse.getStatusCode());
        assertEquals(HttpStatus.CREATED, authorizeResponse.getStatusCode());

        assertEquals(loadRequest.getUserId(), loadResponseBody.getUserId());
        assertEquals(authorizeRequest.getUserId(), authorizeResponseBody.getUserId());

        assertEquals(loadRequest.getMessageId(), loadResponseBody.getMessageId());
        assertEquals(authorizeRequest.getMessageId(), authorizeResponseBody.getMessageId());

        assertEquals(0.0, user.getAccountBalance(), 0.001);
    }

    @Test
    public void testLoadAndAuthorizeTransactionInsufficientBalance() {
        // Load an amount (in this case, it's 50.0)
        LoadRequest loadRequest = new LoadRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
                debitEventFiftyTwoLong.getAmount() + "", "USD", "CREDIT"
            )
        );

        ResponseEntity<?> loadResponse = this.userAccountController.loadTransaction(loadRequest);
        assertTrue(loadResponse.getBody() instanceof LoadResponse);
        LoadResponse loadResponseBody = (LoadResponse) loadResponse.getBody();

        assertEquals(HttpStatus.CREATED, loadResponse.getStatusCode());

        // Authorize a larger amount (in this case, it's 60.0)
        AuthorizationRequest authorizeRequest = new AuthorizationRequest(
            Integer.toString(this.user.getUserId()), "messageId", new RequestAmount(
            (debitEventFiftyTwoLong.getAmount() + 10.0) + "",
            "USD",
            "DEBIT"
            )
        );

        ResponseEntity<?> authorizeResponse = this.userAccountController.authorizeTransaction(authorizeRequest);
        assertTrue(authorizeResponse.getBody() instanceof ErrorResponse);
        ErrorResponse authorizeResponseBody = (ErrorResponse) authorizeResponse.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, authorizeResponse.getStatusCode());
        assertEquals("DECLINED", Objects.requireNonNull(authorizeResponseBody.getCode()));
    }
}