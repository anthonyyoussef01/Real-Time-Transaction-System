package dev.codescreen.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsTest {
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        this.userDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
    }

    // equals() tests ------------------------------------------------
    @Test
    void testEquals_SameUserDetails() {
        assertEquals(this.userDetails, this.userDetails);
        UserDetails anotherUserDetails = new UserDetails(
            "John", "Doe", "john.doe@example.com", "pass"
        );
        assertEquals(this.userDetails, anotherUserDetails);
    }

    @Test
    void testEquals_DifferentUserDetails() {
        UserDetails anotherUserDetails = new UserDetails(
            "Jane", "Doe", "jane.doe@example.com", "pass"
        );
        assertNotEquals(this.userDetails, anotherUserDetails);
    }

    @Test
    void testEquals_Null() {
        assertNotEquals(this.userDetails, null);
    }

    @Test
    void testEquals_DifferentClass() {
        assertNotEquals(this.userDetails, new Object());
    }

    // Setter and Getter tests ------------------------------------------------
    @Test
    void testSetFirstName() {
        this.userDetails.setFirstName("Jane");
        assertEquals("Jane", this.userDetails.getFirstName());
    }

    @Test
    void testSetLastName() {
        this.userDetails.setLastName("Smith");
        assertEquals("Smith", this.userDetails.getLastName());
    }

    @Test
    void testSetEmail() {
        this.userDetails.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", this.userDetails.getEmail());
    }

    @Test
    void testSetHashedPassword() {
        this.userDetails.setHashedPassword("newpass");
        assertEquals("newpass", this.userDetails.getHashedPassword());
    }
}