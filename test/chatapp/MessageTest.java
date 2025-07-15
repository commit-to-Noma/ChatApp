package chatapp;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Before;

public class MessageTest {

    // Reset file and load test cases before each test
    @Before
    public void resetAndSeedFile() throws IOException {
        Files.write(Paths.get("messages.json"), "[]".getBytes());

        new Message("+27718693002", "Hi Mike, can you join us for dinner tonight").sendMessage("Send");
        new Message("08575975889", "Hi Keegan, did you receive the payment?").sendMessage("Disregard");
        new Message("+27834557896", "Did you get the cake?").sendMessage("Send");
        new Message("0838884567", "It is dinner time !").sendMessage("Send");
        new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.").sendMessage("Send");
        new Message("+27838884567", "Ok I am leaving without you.").sendMessage("Store");
        new Message("+27830000000", "This is the longest message sent so far to ensure the method works properly.").sendMessage("Send");
    }

    // Valid message length which is under 250 characters
    @Test
    public void testMessageLength_ValidUnder250Chars() {
        Message msg = new Message("+2771234567", "Short and sweet.");
        assertTrue(msg.isMessageValid());
    }

    // Invalid message length which is over 250 character
    @Test
    public void testMessageLength_Exceeds250Chars() {
        String longMsg = "A".repeat(251);
        Message msg = new Message("+2771234567", longMsg);
        assertFalse(msg.isMessageValid());
    }

    // Valid cell number format which starts with + and has 10–12 digits
    @Test
    public void testRecipientCell_ValidFormat() {
        Message msg = new Message("+2771234567", "Hi");
        assertTrue(msg.checkRecipientCell());
    }

    // Invalid cell number format
    @Test
    public void testRecipientCell_InvalidFormat() {
        Message msg = new Message("08575975889", "Hi");
        assertFalse(msg.checkRecipientCell());
    }

    // Validate correct format of generated message hash
    @Test
    public void testMessageHash_FormatCorrect() {
        Message msg = new Message("+27718693002", "Hi tonight");
        int num = msg.getMessageNumber();
        String expected = msg.getMessageId().substring(0, 2) + ":" + num + ":HITONIGHT";
        assertEquals(expected, msg.createMessageHash());
    }

    // Ensure message ID is not empty
    @Test
    public void testMessageId_GeneratedNotNull() {
        Message msg = new Message("+2771234567", "Check ID");
        assertNotNull(msg.getMessageId());
    }

    // Confirm message sends correctly
    @Test
    public void testSendMessage_SelectedSend_ReturnsSuccess() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message sent.", msg.sendMessage("Send"));
    }

    // Confirm message is disregarded
    @Test
    public void testSendMessage_SelectedDisregard_ReturnsDisregarded() {
        Message msg = new Message("08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Message disregarded.", msg.sendMessage("Disregard"));
    }

    // Confirm message is stored
    @Test
    public void testSendMessage_SelectedStore_ReturnsStored() {
        Message msg = new Message("+27838968976", "Message to store");
        assertEquals("Message stored.", msg.sendMessage("Store"));
    }

    // Validate generated JSON contains message details
    @Test
    public void testStoreMessage_ContainsJsonKeys() {
        Message msg = new Message("+27838968976", "Hello JSON");
        String stored = msg.getJsonEntry("stored");
        assertNotNull("JSON entry should not be null", stored);
        assertTrue(stored.contains("recipient"));
        assertTrue(stored.contains("content"));
        assertTrue(stored.contains("messageHash"));
    }

    // Ensure sent message array contains expected content
    @Test
    public void testSentMessagesArrayContainsCorrectData() {
        Message.loadMessagesFromFile();
        String[] messages = Message.getRecipientAndContentSentMessages();
        assertNotNull("Sent messages array should not be null", messages);

        String combined = String.join(" ", messages);
        assertTrue(combined.contains("Did you get the cake?"));
        assertTrue(combined.toLowerCase().contains("dinner"));
    }

    // Get the longest sent message and validate it
    @Test
    public void testLongestSentMessage() {
        Message longest = Message.getLongestSentMessage();
        assertNotNull("Longest message object should not be null", longest);
        assertTrue(longest.getContent().length() > 25);
    }

    // Search for a message by its ID
    @Test
    public void testSearchByMessageId() throws InterruptedException {
        Message msg = new Message("0838884567", "Search this message please.");
        String id = msg.getMessageId();
        msg.sendMessage("Send");
        Thread.sleep(50); // Allow file to flush

        String result = Message.findMessageById(id);
        assertNotNull("Search result should not be null", result);
        assertTrue(result.contains("Search this message please."));
    }

    // Find messages for a specific recipient
    @Test
    public void testFindMessagesByRecipient() {
        String[] result = Message.findMessagesByRecipient("+27838884567");
        assertNotNull("Recipient result is null", result);

        String all = String.join(" ", result);
        assertTrue(all.contains("Ok I am leaving without you."));
        assertTrue(all.contains("Where are you? You are late! I have asked you to be on time."));
    }

    // Delete a message by its hash value
    @Test
    public void testDeleteMessageByHash() throws InterruptedException {
        Message msg = new Message("+27838884567", "UNIQUE DELETE TEST");
        msg.sendMessage("Store");
        Thread.sleep(50);

        String hash = msg.getMessageHash();
        String[] beforeDelete = Message.findMessagesByRecipient("+27838884567");
        String combinedBefore = String.join(" ", beforeDelete);
        assertTrue("Message was not written before deletion test", combinedBefore.contains("UNIQUE DELETE TEST"));

        boolean deleted = Message.deleteMessageByHash(hash);
        assertTrue("deleteMessageByHash() returned false", deleted);

        String[] afterDelete = Message.findMessagesByRecipient("+27838884567");
        String combinedAfter = String.join(" ", afterDelete);
        assertFalse(combinedAfter.contains("UNIQUE DELETE TEST"));
    }

    // Ensure print report contains necessary fields
    @Test
    public void testSentMessageReportDisplaysCorrectly() {
        String[] lines = Message.printMessage();
        assertNotNull("Report lines should not be null", lines);

        boolean foundRecipient = false;
        boolean foundContent = false;
        boolean foundHash = false;

        for (String line : lines) {
            if (line.contains("Recipient:")) foundRecipient = true;
            if (line.contains("Message:")) foundContent = true;
            if (line.contains("Message Hash:")) foundHash = true;
        }

        assertTrue(foundRecipient);
        assertTrue(foundContent);
        assertTrue(foundHash);
    }

    // Ensure sent messages load correctly from file
    @Test
    public void testLoadSentMessagesFromFileActuallyLoads() {
        Message.loadMessagesFromFile();
        String[] loaded = Message.getRecipientAndContentSentMessages();
        assertTrue("Should have loaded at least one sent message", loaded.length > 0);
    }
}