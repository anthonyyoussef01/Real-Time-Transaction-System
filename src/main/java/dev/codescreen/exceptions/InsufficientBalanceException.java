package dev.codescreen.exceptions;

/*
 * This class represents an InsufficientBalanceException object.
 * An InsufficientBalanceException is thrown when a User tries to perform a DEBIT transaction with an amount greater than
 * their account balance.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}