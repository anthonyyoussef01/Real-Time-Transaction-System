package dev.codescreen;

/*
 * This class represents a User object.
 * A User object will have a UserDetails object and an int account balance.
 * This serves as a Snapshot of the User's account balance currently, and it is the Aggregate Root in the context of the
 * Domain Model.
 */
public class User {
    private UserDetails userDetails;
    private double accountBalance;

    public User(UserDetails userDetails) {
        this.userDetails = userDetails;
        this.accountBalance = 0.0;
    }

    // GETTERS
    public UserDetails getUserDetails() {
        return userDetails;
    }
    public double getAccountBalance() {
        return accountBalance;
    }

    // SETTERS
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}