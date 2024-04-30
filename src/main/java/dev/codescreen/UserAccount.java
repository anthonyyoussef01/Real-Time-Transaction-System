package dev.codescreen;

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
     * Adds a TransactionEvent to the transactionLog and updates the User's account balance.
     * @param event  The event to add to the transactionLog
     */
    public void addTransactionEvent(TransactionEvent event) {
        this.transactionLog.add(event);
        switch (event.getTransactionType()) {
            case CREDIT:
                this.user.setAccountBalance(this.user.getAccountBalance() + event.getAmount());
                break;
            case DEBIT:
                this.user.setAccountBalance(this.user.getAccountBalance() - event.getAmount());
                break;
            default:
                break;
        }
    }
}