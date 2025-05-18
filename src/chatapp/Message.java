/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author nomat
 */
public final class Message {
    private int messageCount = 0;
    private static int totalMessages = 0;
    private static final List<Message> sentMessages = new ArrayList<>(); //Stores sent messages in memory
    private final String messageId;
    private final int messageNumber;
    private String recipient;
    private String content; 
    private final String messageHash;
    
    
public Message(String recipient, String content) {
    this.messageId = generateMessageId();
    this.messageNumber = ++messageCount;
    this.recipient = recipient;
    this.content = content;
    this.messageHash = createMessageHash();
}
    
    private String generateMessageId(){
        //generating a random number for a unique ID
        // ID should only have 10 digits with zeros to make sure it is slways 10 digits 
        return String.format("%010d", new Random().nextInt(1_000_000_000));
    }
    
    public boolean checkRecipientStyle(){
        // recipients tart with + and is <= 10 characters 
        // != null to avoid crash
        return recipient != null && recipient.startsWith("+") && recipient.length() <= 10;
    }
    
    public boolean isMessageValid(){
        // checks if contents is <= 250 characters
        return content != null && content.length() <= 250;
    }
    
    public String createMessageHash() {
        //split("\\s+")	Splits message into words regardless of spacing
        //Ternary (? :)	Used to handle edge cases like empty strings
        //substring(0, 4)	Gets the first part of the message ID
        
        String[] words = content.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (messageId.substring(0, 4) + ":" + messageNumber + ":" + firstWord + "-" + lastWord).toUpperCase();
    }
    
    public String sendMessage() {
        String[] options = {"Send", "Disregard", "Store"};
        int choice = JOptionPane.showOptionDialog(null, "Choose an action for the message:",
                "Message Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0 -> {
                sentMessages.add(this);
                totalMessages++;
                JOptionPane.showMessageDialog(null, printMessage());
                return "Message successfully sent.";
            }
            case 1 -> {
                return "Message disregarded.";
            }
            case 2 -> {
                JOptionPane.showMessageDialog(null, storeMessage());
                return "Message stored (JSON-style).";
            }
            default -> {
                return "No valid option selected.";
            }
        }
    }   
    
    public String printMessage() {
        return "Message ID: " + messageId +
                "\nMessage Hash: " + messageHash +
                "\nRecipient: " + recipient +
                "\nMessage: " + content;
    }    
    
    public static int returnTotalMessages() {
        return totalMessages;
    }

    public static String printAllMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder all = new StringBuilder();
        for (Message msg : sentMessages) {
            all.append(msg.printMessage()).append("\n\n");
        }
        return all.toString();
    }    
    
    public String storeMessage() {
        return "{\n" +
                "  \"messageId\": \"" + messageId + "\",\n" +
                "  \"messageNumber\": " + messageNumber + ",\n" +
                "  \"recipient\": \"" + recipient + "\",\n" +
                "  \"content\": \"" + content + "\",\n" +
                "  \"messageHash\": \"" + messageHash + "\"\n" +
                "}";
    }      
    
    public boolean checkMessageId(){
        //checks if message ID is exactly 10 characters 
        return this.messageId.length()== 10;
    }
        
    //getters 
    public String getMessageId(){
        return messageId;
    }
    
    public int getMessageNumber(){
        return messageNumber;
    }
    
    public String getRecipient(){
        return recipient;
    }
    
    public String getContent(){
        return content;
    }
    
    //setters
    public void setRecipient(String recipient){
        this.recipient = recipient;
    }
    
    public void setContent(String content){
        this.content = content;
    }
    
    public String getMessageHash() {
        return messageHash;
    }
        
}
