package dev.codescreen.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        this.userDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        this.user = new User(userDetails);
    }

    // equals() tests ------------------------------------------------
    @Test
    void testEquals_SameUser() {
        assertEquals(this.user, this.user);
    }

    @Test
    void testEquals_SameUserDetailsAndAccountBalance() {
        UserDetails anotherUserDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        User anotherUser = new User(anotherUserDetails);
        assertEquals(this.user, anotherUser);
    }

    @Test
    void testEquals_DifferentUserDetails() {
        UserDetails anotherUserDetails = new UserDetails(
            "Jane", "Doe", "jane.doe@example.com", "pass"
        );
        User anotherUser = new User(anotherUserDetails);
        assertNotEquals(this.user, anotherUser);
    }

    @Test
    void testEquals_DifferentAccountBalance() {
        UserDetails anotherUserDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        User anotherUser = new User(anotherUserDetails);
        anotherUser.setAccountBalance(100.0);
        assertNotEquals(this.user, anotherUser);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(this.user, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(this.user, new Object());
    }

    // Setter and Getter tests ------------------------------------------------
    @Test
    void testSetAccountBalance() {
        this.user.setAccountBalance(100.0);
        assertEquals(100.0, this.user.getAccountBalance());
    }

    @Test
    void testSetUserDetails() {
        UserDetails anotherUserDetails = new UserDetails(
            "Jane", "Doe", "jane.doe@example.com", "pass"
        );
        this.user.setUserDetails(anotherUserDetails);
        assertEquals(anotherUserDetails, this.user.getUserDetails());
    }
}