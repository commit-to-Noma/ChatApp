package chatapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    //Username format tests
    
    // Test if username contains underscore and ≤5 chars.
    @Test
    public void testUsernameCorrectFormat() {
        User user = new User();
        assertTrue(
            "Should accept 'kyl_1' as valid",
            user.CheckUserName("kyl_1")
        );
    }

    //Test if username has no underscore and >5 chars
    @Test
    public void testUsernameIncorrectFormat() {
        User user = new User();
        assertFalse(
            "Should reject 'kyle!!!!!!!' as invalid",
            user.CheckUserName("kyle!!!!!!!")
        );
    }

    // Password complexity tests
    
    // Test if password has uppercase, digit, special, ≥8 chars.

    @Test
    public void testPasswordCorrectComplexity() {
        User user = new User();
        assertTrue(
            "Should accept 'Ch&&sec@ke99!' as valid",
            user.checkPasswordComplexity("Ch&&sec@ke99!")
        );
    }

    // Test if password has no uppercase, digit, special, ≥8 chars.

    @Test
    public void testPasswordIncorrectComplexity() {
        User user = new User();
        assertFalse(
            "Should reject 'password' as invalid",
            user.checkPasswordComplexity("password")
        );
    }

    // Cell phone format tests
    
    // Test if cell number has an international code.

    @Test
    public void testCellNumberCorrectFormat() {
        User user = new User();
        assertTrue(
            "Should accept '+27838968976' as valid",
            user.checkCellPhoneNumber("+27838968976")
        );
    }

    // Test if cell number doesnt have an international code.

    @Test
    public void testCellNumberIncorrectFormat() {
        User user = new User();
        assertFalse(
            "Should reject '089665553' as invalid",
            user.checkCellPhoneNumber("089665553")
        );
    }

    // Login tests
    
    // loginUser returns true for correct credentials.

    @Test
    public void testLoginSuccessful() {
        User user = new User();
        user.setUsername("kyl_1");
        user.setPassword("Ch&&sec@ke99!");
        assertTrue(
            "Should log in with correct username/password",
            user.loginUser("kyl_1", "Ch&&sec@ke99!")
        );
    }

    // loginUser returns false for incorrect password.

    @Test
    public void testLoginFailed() {
        User user = new User();
        user.setUsername("kyl_1");
        user.setPassword("Ch&&sec@ke99!");
        assertFalse(
            "Should not log in with wrong password",
            user.loginUser("kyl_1", "password")
        );
    }
}
