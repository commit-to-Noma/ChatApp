package chatapp;

import static org.junit.Assert.*;
import org.junit.Test;

public class MessageTest {

    // Test valid message length
    @Test
    public void testMessageLength_ValidUnder250Chars() {
        Message msg = new Message("+2771234567", "Short and sweet.");
        assertTrue("Message should be valid (under 250 characters)", msg.isMessageValid());
    }

    // Test message too long
    @Test
    public void testMessageLength_Exceeds250Chars() {
        String longMsg = "A".repeat(251);
        Message msg = new Message("+2771234567", longMsg);
        assertFalse("Message should be invalid (exceeds 250 characters)", msg.isMessageValid());
    }

    // Test valid recipient format
    @Test
    public void testRecipientCell_ValidFormat() {
        Message msg = new Message("+2771234567", "Hi");
        assertTrue("Valid recipient number format", msg.checkRecipientCell());
    }

    // Test invalid recipient format
    @Test
    public void testRecipientCell_InvalidFormat() {
        Message msg = new Message("08575975889", "Hi");
        assertFalse("Invalid recipient number format", msg.checkRecipientCell());
    }

    // Test message hash format for known input
    @Test
    public void testMessageHash_FormatCorrect() {
        Message msg = new Message("+27718693002", "Hi tonight");
        int actualNumber = msg.getMessageNumber();
        String expectedHash = msg.getMessageId().substring(0, 2) + ":" + actualNumber + ":HITONIGHT";
        assertEquals("Message hash should match expected format", expectedHash, msg.createMessageHash());
    }

    // Test that message ID is auto-generated and not null
    @Test
    public void testMessageId_GeneratedNotNull() {
        Message msg = new Message("+2771234567", "Check ID");
        assertNotNull("Message ID should be generated", msg.getMessageId());
    }

    // Test sending a valid message
    @Test
    public void testSendMessage_SelectedSend_ReturnsSuccess() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message sent.", msg.sendMessage("Send"));
    }

    // Test disregarding a message
    @Test
    public void testSendMessage_SelectedDisregard_ReturnsDisregarded() {
        Message msg = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Message disregarded.", msg.sendMessage("Disregard"));
    }

    // Test storing a message
    @Test
    public void testSendMessage_SelectedStore_ReturnsStored() {
        Message msg = new Message("+27838968976", "Message to store");
        assertEquals("Message stored.", msg.sendMessage("Store"));
    }

    // Test JSON format of stored message
    @Test
    public void testStoreMessage_ContainsJsonKeys() {
        Message msg = new Message("+27838968976", "Hello JSON");
        String stored = msg.storeMessage();
        assertTrue("JSON should contain 'recipient'", stored.contains("recipient"));
        assertTrue("JSON should contain 'content'", stored.contains("content"));
        assertTrue("JSON should contain 'messageHash'", stored.contains("messageHash"));
    }
}
