/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

/**
 *
 * @author nomat
 */
public final class Message {
    
    private static int messageCount = 0;
    private static int totalMessages = 0;
    // A list to store all sent messages 
    private static final List<Message> sentMessages = new ArrayList<>();

    private final String messageId;
    private final int messageNumber;
    private String recipient;
    private String content;
    private final String messageHash;

    // creates a message 
    public Message(String recipient, String content) {
        this.messageId = generateMessageId();
        this.messageNumber = ++messageCount; // increment with each new message
        this.recipient = recipient;
        this.content = content;
        this.messageHash = createMessageHash();
    }
    
    // Generates a unique random number that is always 10 digits and adding zeros if needed 
    private String generateMessageId() {
        return String.format("%010d", new Random().nextInt(1_000_000_000));
    }    

    //make sure message ID is not more 10 characters
    public boolean checkMessageId() {
        return this.messageId.length() <= 10;
    }    
    
    // For checking that cell starts with + and <= 10 character 
    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("\\+\\d{10,12}");
    }    
    
    // Creates a unique hash for the message:
    public String createMessageHash() {
        // Get the first 2 characters of messageId
        String idPart = messageId.substring(0, 2);
        // Clean and split content into words, all in uppercase
        String[] words = content.trim().toUpperCase().split("\\s+");
        // Combine first and last words with no space or dash
        String wordPart = words.length >= 2
            ? words[0] + words[words.length - 1]
            : words[0];  // Handle single-word messages
        return idPart + ":" + messageNumber + ":" + wordPart;
    }
 
    // Ensures message content is no more than 250 characters.
    public boolean isMessageValid() {
        return content != null && content.length() <= 250;
    }    
    
    // Returns a list of all sent messages with info
    public static List<String> printMessage() {
        List<String> messages = new ArrayList<>();
        for (Message msg : sentMessages) {
            messages.add("Message ID: " + msg.messageId +
                         "\nMessage Hash: " + msg.messageHash +
                         "\nRecipient: " + msg.recipient +
                         "\nMessage: " + msg.content);
        }
        return messages;
    }
      
    // Handles message action: send, disregard, or store
    public String sendMessage(ImageIcon javaIcon) {
        String[] choices = {
                "Send Message",
                "Disregard Message",
                "Store Message to Send Later"
        };

        // Ask user what to do with this message
        String selected = (String) JOptionPane.showInputDialog(
                null,
                "Choose what to do with the message:",
                "📤 Send Option",
                JOptionPane.PLAIN_MESSAGE, javaIcon, choices, choices[0]);

        // If user presses cancel or closes box
        if (selected == null) return "No action selected.";

        switch (selected) {
            case "Send Message" -> {
                // Add this message to the sent list and increase count
                sentMessages.add(this);
                totalMessages++;

                // Show confirmation with all details
                JOptionPane.showMessageDialog(
                        null,
                        "📨 Message Successfully Sent\n\n" +
                                "Message ID: " + messageId +
                                "\nMessage Hash: " + messageHash +
                                "\nRecipient: " + recipient +
                                "\nMessage: " + content,
                        "✅ Message Sent",
                        JOptionPane.INFORMATION_MESSAGE, javaIcon);
                return "Message sent.";
            }

            case "Disregard Message" -> {
                // Show discard confirmation
                JOptionPane.showMessageDialog(
                        null,
                        "❌ Message Disregarded...",
                        "Disregarded",
                        JOptionPane.WARNING_MESSAGE);
                return "Message disregarded.";
            }

            case "Store Message to Send Later" -> {  
                // Show message with JSON-style layout 
                JOptionPane.showMessageDialog(
                        null,
                        "📥 Message Stored for Later\n\n" +
                                "Message ID: " + messageId +
                                "\nMessage Hash: " + messageHash +
                                "\nRecipient: " + recipient +
                                "\nMessage: " + content,
                        "🗂️ Stored Message",
                        JOptionPane.INFORMATION_MESSAGE, javaIcon);
                return "Message stored.";
            }

            default -> {
                return "Unknown option.";
            }
        }
    }    
   
    // Method to return how many messages were sent in total
    public static int returnTotalMessages() {
        return totalMessages;
    }    
    
    // returns the message details in a JSON format
    public String storeMessage() {
        return "{\n" +
                "  \"messageId\": \"" + messageId + "\",\n" +
                "  \"messageNumber\": " + messageNumber + ",\n" +
                "  \"recipient\": \"" + recipient + "\",\n" +
                "  \"content\": \"" + content + "\",\n" +
                "  \"messageHash\": \"" + messageHash + "\"\n" +"}";
    }    
    
    //this one is for testing
    public String sendMessage(String action) {
        switch (action.toLowerCase()) {
            case "send":
                sentMessages.add(this);
                totalMessages++;
                return "Message sent.";
            case "disregard":
                return "Message disregarded.";
            case "store":
                return "Message stored.";
            default:
                return "Unknown action.";
        }
    }

    // Getters and Setters for testing 
    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public void setContent(String content) { this.content = content; }
    public String getMessageHash() { return messageHash; }
}
