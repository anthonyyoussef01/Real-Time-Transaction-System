package dev.codescreen.model;

import dev.codescreen.exceptions.InsufficientBalanceException;

import java.util.ArrayList;
import java.util.List;

/*
* This class represents a UserAccount object.
* A UserAccount object will have a User object and a List of TransactionEvent objects.
* This class is considered the Aggregate according to Domain-Driven Design principles. It is the only member of the
* Aggregate that outside objects are allowed to hold references to, and it's responsible for updating the User object.
*/
public class UserAccount {
    private User user;
    private List<TransactionEvent> transactionLog;

    public UserAccount(User user) {
        this.user = user;
        this.transactionLog = new ArrayList<>();
    }

    // GETTERS
    public User getUser() {
        return user;
    }

    public List<TransactionEvent> getTransactionLog() {
        return transactionLog;
    }

    // METHODS

    /**
     * Adds a TransactionEvent to the transactionLog and updates the User's account balance. If the transaction is a
     * debit and the User's account balance is insufficient, an InsufficientBalanceException is thrown.
     *
     * @param event The event to add to the transactionLog
     */
    public void addTransactionEvent(TransactionEvent event) throws InsufficientBalanceException {
        this.transactionLog.add(event);
        switch (event.getTransactionType()) {
            case CREDIT:    // Add money to a user
                this.user.setAccountBalance(this.user.getAccountBalance() + event.getAmount());
                break;
            case DEBIT:     // Conditionally remove money from a user
                if (this.user.getAccountBalance() < event.getAmount()) {
                    // Should this be an exception or should it be handled differently?
                    throw new InsufficientBalanceException("Insufficient balance for a transaction of $" +
                        event.getAmount());
                }
                this.user.setAccountBalance(this.user.getAccountBalance() - event.getAmount());
                break;
            default:
                break;
        }
    }

    /**
     * Recalculates the User's account balance based on the transactionLog.
     *
     * @return double The recalculated account balance
     */
    public double recalculateBalance() {
        double balance = 0.0;
        for (TransactionEvent event : transactionLog) {
            switch (event.getTransactionType()) {
                case CREDIT:
                    balance += event.getAmount();
                    break;
                case DEBIT:
                    balance -= event.getAmount();
                    break;
                default:
                    break;
            }
        }
        this.user.setAccountBalance(balance);
        return balance;
    }
}