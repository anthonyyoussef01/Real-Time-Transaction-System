package dev.codescreen.exceptions;

/*
 * This class represents an IncorrectCurrencyException object.
 * An IncorrectCurrencyException is thrown when a User tries to perform a transaction with a currency that is not the
 * same as the User's account currency.
 */
public class IncorrectCurrencyException extends Exception {
    public IncorrectCurrencyException(String message) {
        super(message);
    }
}
