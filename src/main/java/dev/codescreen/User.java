package dev.codescreen;

/*
* This class represents a User object.
* A User object will have a first name, last name, email, and hashed password.
*/
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        // Hash the password before storing it
        this.hashedPassword = password;
    }

    // GETTERS
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    // SETTERS
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = password;
    }
}
