/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatapp;


import javax.swing.*; // For dialogs, icons, and GUI 
import com.formdev.flatlaf.FlatLightLaf; // Modern UI theme 

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        User user = new User();
        user.registerUser();

        //Login loop
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            isLoggedIn = user.loginUser(); // Console login
            if (!isLoggedIn) {
                JOptionPane.showMessageDialog(null,
                    "❌ Login failed. Please try again.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }        
        
        // Loads an Java logo to be used in the dialogs
        ImageIcon javaIcon = new ImageIcon(Main.class.getResource("/chatapp/java-logo.png"));
        
        
        //welcome message after login
        JOptionPane.showMessageDialog(null,
                "Welcome " + user.getFirstName() + " " + user.getLastName() + ".\nIt is great to see you again!",  // message
                "✅Login Succesful",                  
                JOptionPane.INFORMATION_MESSAGE,
                javaIcon);                      
        
        // Sets the modern looking FlatLaf theme for the entire application
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Shows the first pop-up window when the app starts
        JOptionPane.showMessageDialog(null,
                "👋 Welcome to QuickChat!",       // message
                "Welcome",                       // title of the window
                JOptionPane.INFORMATION_MESSAGE, // icon style
                javaIcon);                       // image icon i loaded

        // A boolean that will keep the app running until the user chooses to quit
        boolean quit = false;

        // The main menu loop. It keeps running until quit is true
        while (!quit) {
            // Shows the main menu popup with 3 numeric options
            String option = (String) JOptionPane.showInputDialog(null,
                    "📋 Select an option by entering the corresponding number:\n" +
                            "1. Send Messages\n" +
                            "2. Show Recently Sent Messages (Coming Soon)\n" +
                            "3. Quit",
                    "📍 Main Menu",               
                    JOptionPane.PLAIN_MESSAGE,   
                    javaIcon,                    
                    null,                        
                    null);                     

            if (option == null) continue; // If user cancels, redisplay menu

            // The value the user typed is evaluated using switch
            switch (option.trim()) {
                case "1" -> {
                    int msgCount;

                    // Asks the user how many messages they want to send
                    try {
                        String input = (String) JOptionPane.showInputDialog(null,
                                "📨 How many messages do you want to send?",
                                "📨 Message Count",
                                JOptionPane.QUESTION_MESSAGE,
                                javaIcon,
                                null,
                                null); // Blank input field

                        if (input == null) continue; // user cancelled
                        msgCount = Integer.parseInt(input.trim()); // error handling with try catch 
                    } catch (NumberFormatException e) {
                        // Handle if the input isn't a valid number
                        JOptionPane.showMessageDialog(null,
                                "🚫 Invalid number. Please enter a valid integer.",
                                "❌ Error",
                                JOptionPane.ERROR_MESSAGE);
                        continue;
                    }

                    // Loop to collect messages from user
                    for (int i = 0; i < msgCount; i++) {
                        String recipient;

                        // Loop until valid recipient phone number is entered
                        while (true) {
                            recipient = (String) JOptionPane.showInputDialog(null,
                                    "📱 Enter recipient cell number (start with +, max 10 chars):",
                                    "📱 Recipient Number",
                                    JOptionPane.QUESTION_MESSAGE,
                                    javaIcon,
                                    null,
                                    null);

                            if (recipient == null) return; // Exit if cancelled

                            // Validate using Message class logic
                            Message temp = new Message(recipient, "temp");
                            if (!temp.checkRecipientCell()) {
                                // Show error if format is wrong
                                JOptionPane.showMessageDialog(null,
                                        "🚫 Invalid recipient!\n✅ Must start with '+' and be 10 characters or fewer.",
                                        "❌ Recipient Error",
                                        JOptionPane.WARNING_MESSAGE);
                            } else break; // valid number, exit loop
                        }

                        String messageText;

                        // Loop until user types a valid message ≤ 250 characters
                        while (true) {
                            messageText = (String) JOptionPane.showInputDialog(null,
                                    "💬 Enter your message (max 250 characters):",
                                    "💬 Message",
                                    JOptionPane.QUESTION_MESSAGE,
                                    javaIcon,
                                    null,
                                    null);

                            if (messageText == null) return;

                            if (messageText.length() > 250) {
                                // Show error if too long
                                JOptionPane.showMessageDialog(null,
                                        "🚫 Message too long.\n📏 Please enter a message under 250 characters.",
                                        "❌ Message Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else break;
                        }

                        // Create message instance and ask user how to handle it (send/store/disregard)
                        Message msg = new Message(recipient, messageText);
                        msg.sendMessage(javaIcon);
                    }

                    // After all messages processed, show summary
                    JOptionPane.showMessageDialog(null,
                            "📊 Message Summary\n Total Messages Created: " + Message.returnTotalMessages(),
                            "✅ Summary",
                            JOptionPane.INFORMATION_MESSAGE,
                            javaIcon);
                }


                case "2" -> {
                    // This option is not implemented, just a placeholder
                    JOptionPane.showMessageDialog(null,
                            "🛠️ Feature in development...\n⏳ Coming Soon...",
                            "🚧 Coming Soon",
                            JOptionPane.INFORMATION_MESSAGE,
                            javaIcon);
                }

                case "3" -> {
                    // Exits the program
                    JOptionPane.showMessageDialog(null,
                            "👋 Goodbye!",
                            "🚪 Exit",
                            JOptionPane.INFORMATION_MESSAGE,
                            javaIcon);
                    quit = true;
                }

                default -> {
                    // Handles anything that's not 1, 2, or 3
                    JOptionPane.showMessageDialog(null,
                            "🚫 Invalid option. Please choose 1, 2 or 3.",
                            "❌ Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
