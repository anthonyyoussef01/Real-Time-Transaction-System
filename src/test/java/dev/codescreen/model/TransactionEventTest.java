package dev.codescreen.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionEventTest {/*
    private TransactionEvent transactionEvent;

    @BeforeEach
    void setUp() {
        this.transactionEvent = new TransactionEvent(1L, TransactionType.CREDIT, 150.0);
    }

    // equals() tests ------------------------------------------------
    @Test
    void testEquals_SameTransactionEvent() {
        assertEquals(this.transactionEvent, this.transactionEvent);
        TransactionEvent anotherTransactionEvent = new TransactionEvent(1L, TransactionType.CREDIT, 150.0);
        assertEquals(this.transactionEvent, anotherTransactionEvent);
    }

    @Test
    void testEquals_DifferentTimestamp() {
        TransactionEvent anotherTransactionEvent = new TransactionEvent(
            2L, TransactionType.CREDIT, 150.0
        );
        assertNotEquals(this.transactionEvent, anotherTransactionEvent);
    }

    @Test
    void testEquals_DifferentTransactionType() {
        TransactionEvent anotherTransactionEvent = new TransactionEvent(
            1L, TransactionType.DEBIT, 150.0
        );
        assertNotEquals(this.transactionEvent, anotherTransactionEvent);
    }

    @Test
    void testEquals_DifferentAmount() {
        TransactionEvent anotherTransactionEvent = new TransactionEvent(
            1L, TransactionType.CREDIT, 200.0
        );
        assertNotEquals(this.transactionEvent, anotherTransactionEvent);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(this.transactionEvent, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(this.transactionEvent, new Object());
    }

    // Setter and Getter tests ------------------------------------------------
    @Test
    void testSetTimestamp() {
        this.transactionEvent.setTimestamp(2L);
        assertEquals(2L, this.transactionEvent.getTimestamp());
    }

    @Test
    void testSetTransactionType() {
        this.transactionEvent.setTransactionType(TransactionType.DEBIT);
        assertEquals(TransactionType.DEBIT, this.transactionEvent.getTransactionType());
    }

    @Test
    void testSetAmount() {
        this.transactionEvent.setAmount(200.0);
        assertEquals(200.0, this.transactionEvent.getAmount());
    }*/
}