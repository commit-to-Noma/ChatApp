/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatapp;
import java.util.Scanner;

/**
 *
 * @author nomat
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = new User();

        // REGISTRATION
        System.out.println("====================");
        System.out.println("   REGISTRATION");
        System.out.println("====================");
        user.registerUser();

        // LOGIN LOOP
        System.out.println("\n====================");
        System.out.println("      LOGIN");
        System.out.println("====================");

        boolean loggedIn = false;
        while (!loggedIn) {
            loggedIn = user.loginUser();
            if (!loggedIn) {
                System.out.println("Login failed. Try again.");
            }
        }

        System.out.println("Welcome to QuickChat, " + user.getFirstName() + " " + user.getLastName() + "!");

        // MAIN MENU LOOP
        int option;
        do {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Send Message");
            System.out.println("2. Show Sent Messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next(); // discard invalid
            }
            option = scanner.nextInt();
            scanner.nextLine(); // clear newline

            switch (option) {
                case 1 -> {
                    System.out.print("How many messages do you want to send? ");
                    int count = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < count; i++) {
                        System.out.print("Enter recipient number (e.g. +2771234567): ");
                        String recipient = scanner.nextLine();

                        System.out.print("Enter your message: ");
                        String content = scanner.nextLine();

                        Message msg = new Message(recipient, content);

                        if (!msg.checkRecipientStyle()) {
                            System.out.println("Invalid recipient format. Try again.");
                            i--;
                            continue;
                        }

                        if (!msg.isMessageValid()) {
                            System.out.println("Message too long. Try again.");
                            i--;
                            continue;
                        }

                        msg.sendMessage();
                    }

                    System.out.println("Total messages sent: " + Message.returnTotalMessages());
                }

                case 2 -> {
                    System.out.println("=== Sent Messages ===");
                    System.out.println(Message.printAllMessages());
                }

                case 3 -> System.out.println("Goodbye!");

                default -> System.out.println("Coming Soon...");
            }

        } while (option != 3);
    }
}