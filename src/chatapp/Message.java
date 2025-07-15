
package chatapp;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Random;

public class Message {

    // File path where messages are stored
    public static final String MESSAGE_FILE_PATH = System.getProperty("user.dir") + "/messages.json";

    // Counters
    private static int messageCount = 0;
    private static int totalMessages = 0;

    // Arrays to track different types of messages
    private static Message[] sentMessages = new Message[100];
    private static Message[] disregardedMessages = new Message[100];
    private static Message[] storedMessages = new Message[100];
    private static Message[] messageHashes = new Message[100];
    private static Message[] messageIds = new Message[100];

    // Parts of a message
    private final String messageId;
    private final int messageNumber;
    private final String messageHash;
    private String recipient;
    private String content;

    // Main constructor for creating new messages
    public Message(String recipient, String content) {
        this.messageId = generateMessageId();
        this.messageNumber = ++messageCount;
        this.recipient = recipient;
        this.content = content;
        this.messageHash = createMessageHash();
    }

    // Constructor used when loading a message from file
    public Message(String recipient, String content, String messageId, int messageNumber, String messageHash) {
        this.recipient = recipient;
        this.content = content;
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.messageHash = messageHash;
    }

    // Getters
    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public String getMessageHash() { return messageHash; }


    // VALIDATION AND MESSAGE DATA HELPERS 

    // Checks that the message ID is 10 characters or less
    public boolean checkMessageId() {
        return this.messageId.length() <= 10;
    }

    // Checks if recipient phone number starts with + and has 10–12 digits
    public boolean checkRecipientCell() {
        return recipient != null && recipient.matches("\\+\\d{10,12}");
    }

    // Checks if message length is within the 250 character limit
    public boolean isMessageValid() {
        return content != null && content.length() <= 250;
    }

    // Generates a random 10-digit message ID
    private String generateMessageId() {
        int randNum = new Random().nextInt(1_000_000_000);
        return String.format("%010d", randNum);
    }

    // Creates a hash using ID, number, and message content
    public String createMessageHash() {
        String idPart = messageId.substring(0, 2);
        String[] words = content.trim().toUpperCase().split("\\s+");
        String wordPart = words.length >= 2 ? words[0] + words[words.length - 1] : words[0];
        return idPart + ":" + messageNumber + ":" + wordPart;
    }

    // MESSAGE ACTION METHODS

    // User chooses whether Send, Store, or Disregard
    public void sendMessage(ImageIcon icon) {
        String[] options = {"Send", "Store", "Disregard"};
        String choice = (String) JOptionPane.showInputDialog(null,
                "Choose what to do with this message:",
                "Message Action",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                options,
                options[0]);

        if (choice == null) return;

        switch (choice.toLowerCase()) {
            case "send" -> {
                addToArray(sentMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                totalMessages++;
                storeMessageToFile("sent");
                JOptionPane.showMessageDialog(null, "✅ Message sent successfully.");
            }
            case "store" -> {
                addToArray(storedMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                storeMessageToFile("stored");
                JOptionPane.showMessageDialog(null, "📦 Message stored successfully.");
            }
            case "disregard" -> {
                addToArray(disregardedMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                JOptionPane.showMessageDialog(null, "🚫 Message disregarded.");
            }
            default -> JOptionPane.showMessageDialog(null, "❌ Unknown action.");
        }
    }

    // Testing version of sendMessage that returns the result as text
    public String sendMessage(String choice) {
        switch (choice.toLowerCase()) {
            case "send" -> {
                addToArray(sentMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                totalMessages++;
                storeMessageToFile("sent");
                return "Message sent.";
            }
            case "store" -> {
                addToArray(storedMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                storeMessageToFile("stored");
                return "Message stored.";
            }
            case "disregard" -> {
                addToArray(disregardedMessages, this);
                addToArray(messageHashes, this);
                addToArray(messageIds, this);
                return "Message disregarded.";
            }
            default -> {
                return "Invalid action.";
            }
        }
    }

    // Converts message to a compact JSON string for testing
    public String getJsonEntry(String type) {
        return String.format(
                "{\"messageId\":\"%s\",\"messageNumber\":%d,\"recipient\":\"%s\",\"content\":\"%s\",\"messageHash\":\"%s\",\"type\":\"%s\"}",
                messageId,
                messageNumber,
                recipient,
                content.replace("\"", "\\\""),
                messageHash,
                type
        );
    }

    // FILE AND DATA HANDLING

    // Adds a message to the first available empty slot in the given array
    private static <T> void addToArray(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = value;
                break;
            }
        }
    }

    // Saves the message to the JSON file based on type; sent or stored
    private void storeMessageToFile(String type) {
        File file = new File(MESSAGE_FILE_PATH);
        String json = "  {\n" +
                "    \"messageId\": \"" + messageId + "\",\n" +
                "    \"messageNumber\": " + messageNumber + ",\n" +
                "    \"recipient\": \"" + recipient + "\",\n" +
                "    \"content\": \"" + content + "\",\n" +
                "    \"messageHash\": \"" + messageHash + "\",\n" +
                "    \"type\": \"" + type + "\"\n" +
                "  }";

        try {
            String fileContent = "";
            if (file.exists()) {
                fileContent = new String(Files.readAllBytes(file.toPath())).trim();
            }

            // Clean JSON writing logic
            if (fileContent.isEmpty() || fileContent.equals("[]")) {
                fileContent = "[\n" + json + "\n]";
            } else {
                // Remove closing bracket
                if (fileContent.endsWith("]")) {
                    fileContent = fileContent.substring(0, fileContent.length() - 1).trim();
                }

                // Ensure no trailing comma in existing content
                if (!fileContent.endsWith("}")) {
                    fileContent += "}";
                }

                fileContent += ",\n" + json + "\n]";
            }

            FileWriter writer = new FileWriter(file, false);
            writer.write(fileContent);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            System.err.println("❌ Error saving message: " + e.getMessage());
        }
    }

    // Helper method to extract field value from JSON-like text
    private static String extractJsonValue(String json, String key) {
        try (BufferedReader reader = new BufferedReader(new StringReader(json))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("\"" + key + "\"")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length < 2) return null;

                    String value = parts[1].trim().replace(",", "").replace("\"", "");
                    return value;
                }
            }
        } catch (Exception e) {
            System.err.println("extractJsonValue error: key=" + key + " | " + e.getMessage());
        }
        return null;
    }
    
    // Loads sent and stored messages from file into memory
    public static void loadMessagesFromFile() {
        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return;

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            content = content.replaceAll("\\},\\s*\\{", "}~{");
            String[] entries = content.split("~");

            for (int i = 0; i < sentMessages.length; i++) sentMessages[i] = null;

            int index = 0;
            for (String entry : entries) {
                String type = extractJsonValue(entry, "type");
                if (type == null || !type.equalsIgnoreCase("sent")) continue;

                String recipient = extractJsonValue(entry, "recipient");
                String contentMsg = extractJsonValue(entry, "content");
                String id = extractJsonValue(entry, "messageId");
                String hash = extractJsonValue(entry, "messageHash");
                String numberStr = extractJsonValue(entry, "messageNumber");

                if (recipient == null || contentMsg == null || id == null || hash == null || numberStr == null) continue;

                int number = Integer.parseInt(numberStr);
                Message msg = new Message(recipient, contentMsg, id, number, hash);

                if (index < sentMessages.length) {
                    sentMessages[index++] = msg;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("❌ Error loading sent messages from file: " + e.getMessage());
        }
    }

    // REPORT AND SEARCH METHODS

    // Returns a summary of all sent messages
    public static String[] printMessage() {
        if (sentMessages == null) return new String[0];

        int count = 0;
        for (Message m : sentMessages) {
            if (m != null) count++;
        }

        String[] result = new String[count];
        int index = 0;

        for (Message m : sentMessages) {
            if (m != null) {
                result[index++] = "Recipient: " + m.recipient + "\n" +
                                  "Message: " + m.content + "\n" +
                                  "Message Hash: " + m.messageHash + "\n" +
                                  "--------------------\n";
            }
        }
        return result;
    }

    //Loading message parts into json according to type
    private static String[] loadMessagesByType(String type) {
        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return new String[0];

            String content = new String(Files.readAllBytes(file.toPath())).trim();

            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            String[] rawChunks = content.split("\\},\\s*\\{");

            String[] entries = new String[rawChunks.length];
            for (int i = 0; i < rawChunks.length; i++) {
                String entry = rawChunks[i];
                if (i == 0) entry += "}";
                else if (i == rawChunks.length - 1) entry = "{" + entry;
                else entry = "{" + entry + "}";
                entries[i] = entry;
            }

            String[] results = new String[entries.length];
            int index = 0;

            for (int i = 0; i < entries.length; i++) {
                String entry = entries[i];

                if (i == 0) entry = entry + "}";
                else if (i == entries.length - 1) entry = "{" + entry;
                else entry = "{" + entry + "}";

                if (!entry.contains("\"type\":\"" + type + "\"")) continue;

                String id = extractJsonValue(entry, "messageId");
                String recipient = extractJsonValue(entry, "recipient");
                String msg = extractJsonValue(entry, "content");
                String hash = extractJsonValue(entry, "messageHash");

                results[index++] = "MessageID: " + id + "\nRecipient: " + recipient +
                        "\nMessage: " + msg + "\nHash: " + hash + "\n------------------\n";
            }

            // Trim the result to only filled entries
            String[] finalResults = new String[index];
            System.arraycopy(results, 0, finalResults, 0, index);
            return finalResults;

        } catch (IOException e) {
            return new String[]{"❌ Error reading file: " + e.getMessage()};
        }
    }   


    // Return all stored messages as strings
    public static String[] printStoredMessages() {
        return loadMessagesByType("stored");
        }


    // Returns array of messages by a specific recipient
    public static String[] findMessagesByRecipient(String searchRecipient) {
        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return new String[0];

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            content = content.replaceAll("\\},\\s*\\{", "}~{");
            String[] entries = content.split("~");

            String[] results = new String[entries.length];
            int index = 0;

            for (String entry : entries) {
                String type = extractJsonValue(entry, "type");
                if (type == null || !(type.equalsIgnoreCase("sent") || type.equalsIgnoreCase("stored"))) continue;

                String recipient = extractJsonValue(entry, "recipient");
                if (recipient == null || !recipient.equals(searchRecipient)) continue;

                String messageId = extractJsonValue(entry, "messageId");
                String messageHash = extractJsonValue(entry, "messageHash");
                String msgContent = extractJsonValue(entry, "content");

                results[index++] = "Message ID: " + messageId +
                        "\nRecipient: " + recipient +
                        "\nMessage: " + msgContent +
                        "\nMessage Hash: " + messageHash +
                        "\n------------------------";
            }

            if (index == 0) return new String[]{"ℹ No messages found for this recipient."};

            String[] finalResults = new String[index];
            System.arraycopy(results, 0, finalResults, 0, index);
            return finalResults;

        } catch (IOException e) {
            return new String[]{"❌ Error reading file: " + e.getMessage()};
        }
    }


    // Finds a message in the JSON file by its message ID
    public static String findMessageById(String idToFind) {
        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return "❌ No messages found.";

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            String[] rawChunks = content.split("\\},\\s*\\{");

    String[] entries = new String[rawChunks.length];
    for (int i = 0; i < rawChunks.length; i++) {
        String entry = rawChunks[i];
        if (i == 0) entry += "}";
        else if (i == rawChunks.length - 1) entry = "{" + entry;
        else entry = "{" + entry + "}";
        entries[i] = entry;
    }

            for (int i = 0; i < entries.length; i++) {
                String entry = entries[i];
                if (i == 0) entry += "}";
                else if (i == entries.length - 1) entry = "{" + entry;
                else entry = "{" + entry + "}";

                String messageId = extractJsonValue(entry, "messageId");

                if (messageId != null && messageId.equals(idToFind)) {
                    String recipient = extractJsonValue(entry, "recipient");
                    String contentMsg = extractJsonValue(entry, "content");
                    String messageHash = extractJsonValue(entry, "messageHash");

                    return "✅ Message Found:\n\n" +
                           "Message ID: " + messageId + "\n" +
                           "Message Hash: " + messageHash + "\n" +
                           "Recipient: " + recipient + "\n" +
                           "Message: " + contentMsg;
                }
            }

            return "❌ No message with that ID found.";

        } catch (IOException e) {
            return "❌ Error: " + e.getMessage();
        }
    }

    // Deletes a message from file based on message hash
    public static boolean deleteMessageByHash(String hashToDelete) {
        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return false;

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.isEmpty() || content.equals("[]")) return false;

            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            String[] rawChunks = content.split("\\},\\s*\\{");

            String[] entries = new String[rawChunks.length];
            for (int i = 0; i < rawChunks.length; i++) {
                String entry = rawChunks[i];
                if (i == 0) entry += "}";
                else if (i == rawChunks.length - 1) entry = "{" + entry;
                else entry = "{" + entry + "}";
                entries[i] = entry;
            }
            StringBuilder newContent = new StringBuilder();
            boolean deleted = false;

            for (int i = 0; i < entries.length; i++) {
                String entry = entries[i];
                if (i == 0) entry += "}";
                else if (i == entries.length - 1) entry = "{" + entry;
                else entry = "{" + entry + "}";

                String entryHash = extractJsonValue(entry, "messageHash");
                if (entryHash != null && entryHash.equals(hashToDelete)) {
                    deleted = true;
                    continue; // skip matching entry
                }

                if (newContent.length() > 0) newContent.append(",\n");
                newContent.append(entry);
            }

            // Write cleaned JSON back
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[\n" + newContent.toString() + "\n]");
            }

            return deleted;

        } catch (IOException e) {
            System.err.println("❌ Error deleting message: " + e.getMessage());
            return false;
        }
    }


    //Returns the message with the longest content
    public static Message getLongestSentMessage() {
        int maxLength = 0;
        Message longest = null;

        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return null;

            String content = new String(Files.readAllBytes(file.toPath())).trim();

            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            String[] rawChunks = content.split("\\},\\s*\\{");

        String[] entries = new String[rawChunks.length];
        for (int i = 0; i < rawChunks.length; i++) {
            String entry = rawChunks[i];
            if (i == 0) entry += "}";
            else if (i == rawChunks.length - 1) entry = "{" + entry;
            else entry = "{" + entry + "}";
            entries[i] = entry;
        }

                for (int i = 0; i < entries.length; i++) {
                    String entry = entries[i];
                    if (i == 0) entry += "}";
                    else if (i == entries.length - 1) entry = "{" + entry;
                    else entry = "{" + entry + "}";

                    String type = extractJsonValue(entry, "type");

                    System.out.println("🔍 ENTRY: " + entry);
                    System.out.println("Type: " + extractJsonValue(entry, "type"));
                    System.out.println("Recipient: " + extractJsonValue(entry, "recipient"));

                    // ⬇ Now includes "sent" OR "stored"
                    if (type == null || !(type.equalsIgnoreCase("sent") || type.equalsIgnoreCase("stored"))) continue;

                    String recipient = extractJsonValue(entry, "recipient");
                    String msgContent = extractJsonValue(entry, "content");
                    String id = extractJsonValue(entry, "messageId");
                    String hash = extractJsonValue(entry, "messageHash");
                    String numberStr = extractJsonValue(entry, "messageNumber");

                    if (msgContent != null && msgContent.length() > maxLength) {
                        maxLength = msgContent.length();
                        int number = Integer.parseInt(numberStr);
                        longest = new Message(recipient, msgContent, id, number, hash);
                    }
                }

            } catch (IOException | NumberFormatException e) {
                System.err.println("❌ Error reading longest message: " + e.getMessage());
            }

            return longest;
        }

    // Returns number of messages based on type (sent/stored/disregarded)
    public static int countByType(String type) {
        int count = 0;

        try {
            File file = new File(MESSAGE_FILE_PATH);
            if (!file.exists()) return 0;

            String content = new String(Files.readAllBytes(file.toPath())).trim();
            if (content.startsWith("[")) content = content.substring(1);
            if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

            content = content.replaceAll("\\},\\s*\\{", "}~{");
            String[] entries = content.split("~");

            for (String entry : entries) {
                String entryType = extractJsonValue(entry, "type");
                if (entryType != null && entryType.equalsIgnoreCase(type)) {
                    count++;
                }
            }

        } catch (IOException e) {
            System.err.println("❌ Error in countByType: " + e.getMessage());
        }

        return count;
    }

    // Creates a full summary report of all sent messages
    public static String generateSentReport() {
            StringBuilder report = new StringBuilder();
            report.append("📄 Sent Message Report\n\n");

            try {
                File file = new File(MESSAGE_FILE_PATH);
                if (!file.exists()) return "❌ No messages found.";

                String content = new String(Files.readAllBytes(file.toPath())).trim();

                if (content.startsWith("[")) content = content.substring(1);
                if (content.endsWith("]")) content = content.substring(0, content.length() - 1);

                String[] rawChunks = content.split("\\},\\s*\\{");

                String[] entries = new String[rawChunks.length];
                for (int i = 0; i < rawChunks.length; i++) {
                    String entry = rawChunks[i];
                    if (i == 0) entry += "}";
                    else if (i == rawChunks.length - 1) entry = "{" + entry;
                    else entry = "{" + entry + "}";
                    entries[i] = entry;
                }
                int count = 0;

                for (int i = 0; i < entries.length; i++) {
                    String entry = entries[i];

                    // Fix JSON brackets lost during split
                    if (i == 0) entry = entry + "}";
                    else if (i == entries.length - 1) entry = "{" + entry;
                    else entry = "{" + entry + "}";

                    if (!entry.contains("\"type\":\"sent\"")) continue;

                    String messageId = extractJsonValue(entry, "messageId");
                    String messageNumber = extractJsonValue(entry, "messageNumber");
                    String recipient = extractJsonValue(entry, "recipient");
                    String messageHash = extractJsonValue(entry, "messageHash");

                    count++;
                    report.append("Message #: ").append(messageNumber).append("\n");
                    report.append("Recipient: ").append(recipient).append("\n");
                    report.append("Message ID: ").append(messageId).append("\n");
                    report.append("Message Hash: ").append(messageHash).append("\n");
                    report.append("---------------------------\n");
                }

                if (count == 0) return "ℹ No sent messages found.";

            } catch (IOException e) {
                return "❌ Error reading messages.json: " + e.getMessage();
            }

            return report.toString();
        }


        // Creates a visual report from in-memory sent messages
        public static String getDisplayReport() {
            StringBuilder report = new StringBuilder();
            report.append("📄 Display Report\n\n");

            boolean found = false;
            for (Message msg : sentMessages) {
                if (msg != null) {
                    found = true;
                    report.append("Message ID: ").append(msg.getMessageId()).append("\n");
                    report.append("Recipient: ").append(msg.getRecipient()).append("\n");
                    report.append("Content: ").append(msg.getContent()).append("\n");
                    report.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
                    report.append("-------------------\n");
                }
            }

            if (!found) {
                return "No sent messages to display.";
            }

            return report.toString();
        }

    // Returns a list of sent message previews (used in GUI)
    public static String[] getRecipientAndContentSentMessages() {
        if (sentMessages == null) return new String[0]; // avoid null pointer

        int count = 0;
        for (Message m : sentMessages) {
            if (m != null) count++;
        }

        String[] result = new String[count];
        int index = 0;
        for (Message m : sentMessages) {
            if (m != null) {
                result[index++] = "Recipient: " + m.recipient + "\nMessage: " + m.content + "\n--------------------\n";
            }
        }
        return result;
        }
    
    
    }