package com.ticketpurchaseapp.purchase.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UtilityTest {

    @Test
    public void testEmailWhitelist_ValidEmail() {
        Utility utility = new Utility();
        assertTrue(utility.emailWhitelist("test@example.com"));
    }

    @Test
    public void testEmailWhitelist_InvalidEmail() {
        Utility utility = new Utility();
        assertFalse(utility.emailWhitelist("invalid_email"));
    }

    @Test
    public void testEmailWhitelist_NullEmail() {
        Utility utility = new Utility();
        assertFalse(utility.emailWhitelist(null));
    }

    @Test
    public void testIsInputSafe_SafeInput() {
        Utility utility = new Utility();
        assertTrue(utility.isInputSafe("This is a safe input."));
    }

    @Test
    public void testIsInputSafe_DangerousInput() {
        Utility utility = new Utility();
        assertFalse(utility.isInputSafe("<script>alert('Hello, World!');</script>"));
    }

    @Test
    public void testIsInputSafe_NullInput() {
        Utility utility = new Utility();
        assertFalse(utility.isInputSafe(null));
    }

    @Test
    public void testGenerateRandomUUID() {
        Utility utility = new Utility();
        String uuid = utility.generateRandomUUID();

        assertNotNull(uuid);
        // Validate that the generated UUID is in a standard format
        assertTrue(uuid.matches("^[0-9a-fA-F-]{36}$"));
    }
}
