package dev.codescreen.model;

/*
 * This class represents a User object.
 * A User object will have a UserDetails object, a double account balance, and a unique userId.
 * This serves as a Snapshot of the User's account balance currently, and it is the Aggregate Root in the context of the
 * Domain Model.
 */
public class User {
    private final int userId;
    private UserDetails userDetails;
    private double accountBalance;

    public User(UserDetails userDetails) {
        this.userId = userDetails.hashCode();
        this.userDetails = userDetails;
        this.accountBalance = 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Double.compare(user.accountBalance, this.accountBalance) == 0 &&
               this.userDetails.equals(user.userDetails);
    }

    // GETTERS ------------------------------------------------
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public int getUserId() {
        return userId;
    }

    // SETTERS ------------------------------------------------
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}