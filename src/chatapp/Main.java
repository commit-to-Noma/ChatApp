package chatapp;

import javax.swing.*;
import java.io.IOException;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Dimension;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load app icon
        ImageIcon javaIcon = new ImageIcon(Main.class.getResource("/chatapp/java-logo.png"));

        // Welcome first
        JOptionPane.showMessageDialog(null,
                "👋 Welcome to QuickChat!",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE,
                javaIcon);

        // Register user
        User user = new User();
        String registrationResult = user.registerUser();
        if (!registrationResult.equals("Success")) {
            JOptionPane.showMessageDialog(null,
                    "Registration cancelled. Exiting...",
                    "Registration",
                    JOptionPane.WARNING_MESSAGE,
                    javaIcon);
            return;
        }

        // Login loop
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            isLoggedIn = user.loginUser();
            if (!isLoggedIn) {
                JOptionPane.showMessageDialog(null,
                        "❌ Login failed. Please try again.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE,
                        javaIcon);
            }
        }

        // Personalized welcome after login
        JOptionPane.showMessageDialog(null,
                "Welcome " + user.getFirstName() + " " + user.getLastName() + ".\nIt is great to see you again!",
                "✅ Login Successful",
                JOptionPane.INFORMATION_MESSAGE,
                javaIcon);


        // Main loop
        boolean quit = false;
        while (!quit) {
            String mainOption = (String) JOptionPane.showInputDialog(null,
                    "📋 Select an option by entering the corresponding number:\n\n" +
                            "1. ✉ Send Messages\n" +
                            "2. 📤 Show Recently Sent Messages\n" +
                            "3. ❌ Quit",
                    "📍 Main Menu",
                    JOptionPane.PLAIN_MESSAGE,
                    javaIcon,
                    null,
                    null);

            if (mainOption == null) continue;

            switch (mainOption.trim()) {

                // Sending Messages
                case "1" -> {
                    String input = JOptionPane.showInputDialog(null,
                            "How many messages would you like to process?",
                            "✉ Send Messages",
                            JOptionPane.QUESTION_MESSAGE);

                    if (input == null || input.trim().isEmpty()) return;

                    int count;
                    try {
                        count = Integer.parseInt(input.trim());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null,
                                "❌ Please enter a valid number.",
                                "Invalid Input",
                                JOptionPane.ERROR_MESSAGE,
                                javaIcon);
                        return;
                    }

                    for (int i = 0; i < count; i++) {

                        // Ask for valid recipient number
                        String recipient;
                        while (true) {
                            recipient = JOptionPane.showInputDialog(null,
                                    "Enter recipient number:",
                                    "📱 Recipient",
                                    JOptionPane.QUESTION_MESSAGE);

                            if (recipient == null) return;

                            Message temp = new Message(recipient.trim(), "test");
                            if (!temp.checkRecipientCell()) {
                                JOptionPane.showMessageDialog(null,
                                        "❌ Invalid number. Must start with '+' and be 10–12 digits long.",
                                        "Recipient Error",
                                        JOptionPane.ERROR_MESSAGE,
                                        javaIcon);
                            } else {
                                break;
                            }
                        }

                        // Ask for message content
                        String content;
                        while (true) {
                            content = JOptionPane.showInputDialog(null,
                                    "Enter your message:",
                                    "📝 Message Content",
                                    JOptionPane.QUESTION_MESSAGE);

                            if (content == null) return;

                            if (content.length() > 250) {
                                JOptionPane.showMessageDialog(null,
                                        "❌ Message too long! Must be under 250 characters.",
                                        "Message Error",
                                        JOptionPane.ERROR_MESSAGE,
                                        javaIcon);
                            } else {
                                break;
                            }
                        }

                        // Create and send the message
                        Message message = new Message(recipient.trim(), content.trim());
                        message.sendMessage(javaIcon);

                        // Show live count summary
                        Message.loadMessagesFromFile();
                        int sentCount = Message.countByType("sent");
                        int storedCount = Message.countByType("stored");

                        JOptionPane.showMessageDialog(null,
                                "📊 Live Summary:\n\n" +
                                        "✅ Sent: " + sentCount + "\n" +
                                        "📦 Stored: " + storedCount,
                                "Live Summary",
                                JOptionPane.INFORMATION_MESSAGE,
                                javaIcon);
                    }
                }

                // Sent Messages Submenu
                case "2" -> {
                    boolean backToMain = false;
                    while (!backToMain) {
                        String submenuOption = (String) JOptionPane.showInputDialog(null,
                                "📬 What would you like to do?\n\n" +
                                        "A. 📑 View All Sent Messages\n" +
                                        "B. 📝 View Longest Sent Message\n" +
                                        "C. 🔍 Search by Message ID\n" +
                                        "D. 🔎 Search Messages by Recipient\n" +
                                        "E. ❌ Delete a Message by Message Hash\n" +
                                        "F. 📊 View Message Report\n" +
                                        "G. 🔙 Back to Main Menu",
                                "📤 Sent Messages Menu",
                                JOptionPane.PLAIN_MESSAGE,
                                javaIcon,
                                null,
                                null);

                        if (submenuOption == null) continue;

                        switch (submenuOption.trim().toUpperCase()) {

                            case "A" -> {
                                Message.loadMessagesFromFile();
                                String[] messages = Message.getRecipientAndContentSentMessages();

                                if (messages.length == 0) {
                                    JOptionPane.showMessageDialog(null,
                                            "No sent messages found.",
                                            "Sent Messages",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JTextArea textArea = new JTextArea();
                                    textArea.setEditable(false);
                                    for (String msg : messages) {
                                        textArea.append(msg);
                                    }
                                    JScrollPane scrollPane = new JScrollPane(textArea);
                                    scrollPane.setPreferredSize(new Dimension(500, 400));

                                    JOptionPane.showMessageDialog(null,
                                            scrollPane,
                                            "Sent Messages",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                            }

                            case "B" -> {
                                Message longest = Message.getLongestSentMessage();
                                if (longest == null) {
                                    JOptionPane.showMessageDialog(null,
                                            "No sent messages found.",
                                            "Longest Sent Message",
                                            JOptionPane.INFORMATION_MESSAGE,
                                            javaIcon);
                                } else {
                                    String output = "Longest Sent Message:\n\n" +
                                            "Message ID: " + longest.getMessageId() + "\n" +
                                            "Message Hash: " + longest.getMessageHash() + "\n" +
                                            "Recipient: " + longest.getRecipient() + "\n" +
                                            "Message: " + longest.getContent();
                                    JOptionPane.showMessageDialog(null,
                                            output,
                                            "Longest Sent Message",
                                            JOptionPane.INFORMATION_MESSAGE,
                                            javaIcon);
                                }
                            }

                            case "C" -> {
                                String inputId = JOptionPane.showInputDialog(null,
                                        "Enter the message ID to search for:",
                                        "Input",
                                        JOptionPane.QUESTION_MESSAGE);

                                if (inputId == null || inputId.trim().isEmpty()) continue;

                                String result = Message.findMessageById(inputId.trim());

                                if (result.startsWith("✅")) {
                                    String messageDetails = result.replaceFirst("✅ Message Found:\n\n", "");
                                    JOptionPane.showMessageDialog(null,
                                            messageDetails,
                                            "Message Found",
                                            JOptionPane.INFORMATION_MESSAGE,
                                            javaIcon);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            result,
                                            "Message Not Found",
                                            JOptionPane.WARNING_MESSAGE,
                                            javaIcon);
                                }
                            }

                            case "D" -> {
                                String recipientInput = JOptionPane.showInputDialog(null,
                                        "Enter the recipient to search for:",
                                        "Input",
                                        JOptionPane.QUESTION_MESSAGE);

                                if (recipientInput == null || recipientInput.trim().isEmpty()) continue;

                                String[] rawResults = Message.findMessagesByRecipient(recipientInput.trim());

                                if (rawResults.length == 1 && rawResults[0].startsWith("❌")) {
                                    JOptionPane.showMessageDialog(null,
                                            rawResults[0],
                                            "No Messages Found",
                                            JOptionPane.WARNING_MESSAGE,
                                            javaIcon);
                                    continue;
                                }

                                StringBuilder result = new StringBuilder();
                                result.append("Messages Sent to: ").append(recipientInput.trim()).append("\n\n");

                                for (String entry : rawResults) {
                                    String message = "", messageId = "", messageHash = "";
                                    String[] lines = entry.split("\n");

                                    for (String line : lines) {
                                        if (line.startsWith("Message:")) {
                                            message = line.substring(8).trim();
                                            Message dummy = new Message(recipientInput.trim(), message);
                                            messageId = dummy.getMessageId();
                                            messageHash = dummy.getMessageHash();
                                        }
                                    }

                                    result.append("Message ID: ").append(messageId).append("\n");
                                    result.append("Message Hash: ").append(messageHash).append("\n");
                                    result.append("Message: ").append(message).append("\n\n");
                                }

                                JTextArea area = new JTextArea(result.toString());
                                area.setEditable(false);
                                JScrollPane scroll = new JScrollPane(area);
                                scroll.setPreferredSize(new Dimension(500, 400));

                                JOptionPane.showMessageDialog(null,
                                        scroll,
                                        "Messages to Recipient",
                                        JOptionPane.INFORMATION_MESSAGE,
                                        javaIcon);
                            }

                            case "E" -> {
                                String hashToDelete = JOptionPane.showInputDialog(null,
                                        "Enter the message hash to delete:",
                                        "Input",
                                        JOptionPane.QUESTION_MESSAGE);

                                                                if (hashToDelete == null || hashToDelete.trim().isEmpty()) break;

                                boolean deleted = Message.deleteMessageByHash(hashToDelete.trim());

                                if (deleted) {
                                    JOptionPane.showMessageDialog(null,
                                            "Message deleted successfully.",
                                            "Deleted",
                                            JOptionPane.INFORMATION_MESSAGE,
                                            javaIcon);
                                } else {
                                    JOptionPane.showMessageDialog(null,
                                            "No message found with the given hash.",
                                            "Message Not Found",
                                            JOptionPane.WARNING_MESSAGE,
                                            javaIcon);
                                }
                            }

                            case "F" -> {
                                Message.loadMessagesFromFile();
                                String report = Message.getDisplayReport();

                                if (report.startsWith("❌") || report.startsWith("ℹ") || report.startsWith("No sent messages")) {
                                    JOptionPane.showMessageDialog(null,
                                            report,
                                            "Sent Messages Report",
                                            JOptionPane.INFORMATION_MESSAGE,
                                            javaIcon);
                                    break;
                                }

                                JTextArea area = new JTextArea(report);
                                area.setEditable(false);
                                JScrollPane scroll = new JScrollPane(area);
                                scroll.setPreferredSize(new Dimension(500, 400));

                                JOptionPane.showMessageDialog(null,
                                        scroll,
                                        "Sent Messages Report",
                                        JOptionPane.INFORMATION_MESSAGE,
                                        javaIcon);
                            }

                            case "G" -> backToMain = true;

                            default -> JOptionPane.showMessageDialog(null,
                                    "❌ Invalid option. Please choose a valid letter (A–G).",
                                    "Invalid Input",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

                // Exit the application
                case "3" -> {
                    JOptionPane.showMessageDialog(null,
                            "👋 Goodbye!",
                            "Exit",
                            JOptionPane.INFORMATION_MESSAGE,
                            javaIcon);
                    quit = true;
                }

                default -> JOptionPane.showMessageDialog(null,
                        "❌ Invalid input. Please enter 1, 2, or 3.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

                               