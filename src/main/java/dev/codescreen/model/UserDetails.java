package dev.codescreen.model;

/*
 * This class represents a UserDetails object.
 * A UserDetails object will have a String first name, String last name, String email, and String hashed password.
 */
public class UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String hashedPassword;

    public UserDetails(String firstName, String lastName, String email, String password) {
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
        // Hash the password before storing it
        this.hashedPassword = password;
    }
}