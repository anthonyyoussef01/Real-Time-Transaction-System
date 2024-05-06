package dev.codescreen.model;

import dev.codescreen.exceptions.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountTest {
    private UserAccount userAccount;
    private UserDetails userDetails;
    private User user;
    private TransactionEvent creditEvent;
    private TransactionEvent debitEvent;

    @BeforeEach
    void setUp() {
        this.userDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        this.user = new User(userDetails);
        this.userAccount = new UserAccount(user, "USD");
        this.creditEvent = new TransactionEvent(1L, TransactionType.CREDIT, 150.0, "2341", "USD");
        this.debitEvent = new TransactionEvent(2L, TransactionType.DEBIT, 50.0, "2342", "USD");
    }

    // addTransactionEvent() tests ------------------------------------------------
    @Test
    void testAddTransactionEvent_Credit() throws InsufficientBalanceException {
        this.userAccount.addTransactionEvent(this.creditEvent);                   // +150.0
        assertEquals(150.0, this.user.getAccountBalance());
        assertTrue(this.userAccount.getTransactionLog().contains(this.creditEvent));
    }

    @Test
    void testAddTransactionEvent_Debit() throws InsufficientBalanceException {
        this.userAccount.addTransactionEvent(this.creditEvent);                   // +150.0
        this.userAccount.addTransactionEvent(this.debitEvent);                    // -50.0
        assertEquals(100.0, this.user.getAccountBalance());
        assertTrue(this.userAccount.getTransactionLog().contains(this.debitEvent));
    }

    @Test
    void testAddTransactionEvent_InsufficientBalance() {
        assertThrows(
            InsufficientBalanceException.class,
            () -> this.userAccount.addTransactionEvent(this.debitEvent)           // -50.0
        );
    }

    @Test
    void testAddTransactionEvent_NullTransactionType() {
        TransactionEvent event = new TransactionEvent(
            1L, null, 150.0, "2341", "USD"
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> this.userAccount.addTransactionEvent(event)
        );
    }

    @Test
    void testAddTransactionEvent_NegativeAmount() {
        TransactionEvent event = new TransactionEvent(
            1L, TransactionType.CREDIT, -150.0, "2341", "USD"
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> this.userAccount.addTransactionEvent(event)
        );
    }

    @Test
    void testAddTransactionEvent_ZeroAmount() throws InsufficientBalanceException {
        TransactionEvent event = new TransactionEvent(
            1L, TransactionType.CREDIT, 0.0, "2341", "USD"
        );
        this.userAccount.addTransactionEvent(event);
        assertEquals(0.0, this.user.getAccountBalance());
        assertTrue(this.userAccount.getTransactionLog().contains(event));
    }

    // recalculateBalance() tests ------------------------------------------------
    @Test
    void testRecalculateBalance() throws InsufficientBalanceException {
        TransactionEvent creditEvent = new TransactionEvent(
            System.currentTimeMillis(), TransactionType.CREDIT, 200.0, "2341", "USD"
        );
        TransactionEvent debitEvent = new TransactionEvent(
            System.currentTimeMillis(), TransactionType.DEBIT, 200.0, "2342", "USD"
        );
        this.userAccount.addTransactionEvent(creditEvent);
        this.userAccount.addTransactionEvent(debitEvent);
        assertEquals(
            0.0, this.userAccount.recalculateBalance(),
            "The balance should be 0.0 after adding a credit and a debit of the same amount"
        );
    }

    @Test
    void testRecalculateBalance_AfterIncorrectBalance() throws InsufficientBalanceException {
        this.userAccount.addTransactionEvent(this.creditEvent);                   // +150.0
        this.userAccount.addTransactionEvent(this.debitEvent);                    // -50.0
        this.user.setAccountBalance(200.0);
        this.userAccount.recalculateBalance();
        assertEquals(
            100.0, this.userAccount.recalculateBalance(),
            "The balance should be correct after recalculating the balance, " +
                "even if the User's account balance was previously incorrect"
        );
    }

    @Test
    void testRecalculateBalance_EmptyTransactionLog() {
        assertEquals(
            0.0, this.userAccount.recalculateBalance(),
            "The balance should be 0.0 if the transaction log is empty"
        );
    }

    @Test
    void testRecalculateBalance_EmptyTransactionLogWithIncorrectBalance() throws InsufficientBalanceException {
        this.user.setAccountBalance(200.0);
        assertEquals(
            0.0, this.userAccount.recalculateBalance()
        );
        this.user.setAccountBalance(0.1);
        this.userAccount.addTransactionEvent(new TransactionEvent(
            System.currentTimeMillis(), TransactionType.CREDIT, 0.0, "2341", "USD"
        ));
        assertEquals(
            0.0, this.userAccount.recalculateBalance(),
            "The balance should be 0.0 if the transaction log is empty, " +
                "even if the User's account balance was previously incorrect"
        );
    }

    // equals() tests ------------------------------------------------
    @Test
    void testEquals_SameUserAndTransactionLog() {
        UserAccount anotherUserAccount = new UserAccount(user, "USD");
        anotherUserAccount.getTransactionLog().add(creditEvent);
        userAccount.getTransactionLog().add(creditEvent);
        assertEquals(userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_SameUserReorderedTransactionLog() {
        UserAccount anotherUserAccount = new UserAccount(user, "USD");
        anotherUserAccount.getTransactionLog().add(creditEvent);
        anotherUserAccount.getTransactionLog().add(debitEvent);
        userAccount.getTransactionLog().add(debitEvent);
        userAccount.getTransactionLog().add(creditEvent);
        assertEquals(userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_SameUserAndSameEmptyTransactionLog() {
        UserAccount anotherUserAccount = new UserAccount(user, "USD");
        assertEquals(userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_SameUserAndDifferentTransactionLog() {
        UserAccount anotherUserAccount = new UserAccount(user, "USD");
        TransactionEvent anotherCreditEvent = new TransactionEvent(
            3L, TransactionType.CREDIT, 200.0, "2341", "USD"
        );
        anotherUserAccount.getTransactionLog().add(anotherCreditEvent);
        anotherUserAccount.getTransactionLog().add(debitEvent);
        assertNotEquals(userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_DuplicateUser() throws InsufficientBalanceException {
        UserDetails anotherUserDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        User anotherUser = new User(anotherUserDetails);
        UserAccount anotherUserAccount = new UserAccount(anotherUser, "USD");
        anotherUserAccount.addTransactionEvent(this.creditEvent);
        this.userAccount.addTransactionEvent(this.creditEvent);
        assertEquals(this.userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_DifferentUser() {
        User anotherUser = new User(this.userDetails);
        UserAccount anotherUserAccount = new UserAccount(anotherUser, "USD");
        anotherUserAccount.getTransactionLog().add(this.creditEvent);
        anotherUserAccount.getTransactionLog().add(this.debitEvent);
        assertNotEquals(this.userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_DifferentUserAndTransactionLog() {
        User anotherUser = new User(this.userDetails);
        UserAccount anotherUserAccount = new UserAccount(anotherUser, "USD");
        TransactionEvent anotherCreditEvent = new TransactionEvent(
            3L, TransactionType.CREDIT, 200.0, "2341", "USD"
        );
        anotherUserAccount.getTransactionLog().add(anotherCreditEvent);
        anotherUserAccount.getTransactionLog().add(debitEvent);
        assertNotEquals(userAccount, anotherUserAccount);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(this.userAccount, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(this.userAccount, new Object());
        assertNotEquals(this.userAccount, this.user);
    }
}
