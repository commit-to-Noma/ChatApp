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
//This class is for storing the private account details  
public class User {
    private String username;
    private String password;
    private String cellnumber;
    private String firstname;
    private String lastname;
    

    // Getter methods for testing
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellnumber() {
        return cellnumber;
    }
    
    public String getFirstName() {
        return firstname;
    }
    
    public String getLastName() {
        return lastname;
    }
    
    //adding these methods to allow for setting values during testing
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellnumber(String cellnumber) {
        this.cellnumber = cellnumber;
    }
    
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }
    
    public void setLastName(String lastname) {
        this.lastname = lastname;
    }
    
    //method to check it username is valid
    public boolean CheckUserName(String username){
        //username should contain an underscore and no more the 5 character long 
        return username.contains("_") && username.length() <= 5;
    }
    
    //method to check it username is valid
    public boolean checkPasswordComplexity(String password){
        //password should have at least 8 characters, 1 capital letter, 1 integer, and 1 special character 
        if (password.length() < 8){
            return false;
        }
         boolean hasUpper = false ;
         boolean hasDigit = false ;
         boolean hasSpecial = false ;
        
        //going through each index of the password
        for (int i = 0; i < password.length(); i= i + 1){
            //getting character in positon i and storing it in letter
            char letter = password.charAt(i);
            //checking if character is uppercase
            if (Character.isUpperCase(letter)){
                hasUpper = true;
            }
            //check is character is a number 
            if (Character.isDigit(letter)){
                hasDigit = true;
            }
            // if its not a letter or number it must be a special character 
            if (!Character.isLetterOrDigit(letter)){
                hasSpecial = true;
            }
        }
        return hasUpper && hasDigit && hasSpecial ;
    }
    
    // This cell phone validation logic was generated with the assistance of ChatGPT.
    // OpenAI. (2023). ChatGPT (Mar 14 version) [Large language model]. https://chat.openai.com/chat
    //method to check if phone number is valid based on regex 
    public boolean checkCellPhoneNumber(String cellnumber){
        // Regex pattern for international country code and 10 digits 
        //\\d means digit and $ means end of string
        String regex = "\\+\\d{1,3}\\d{10}$"; 
        // uses matches() to check if the cell number matches the regex conditions
        return cellnumber.matches(regex);
    }
        
    public String registerUser() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter First Name: " );
        firstname = input.nextLine();
        
        System.out.print("Enter Last Name: " );
        lastname = input.nextLine();
        
        
        // Loop until valid username
        while (true) {
            System.out.print("Enter Username: ");
            username = input.nextLine();
            if (CheckUserName(username)) {
                System.out.println("Username successfully captured");
                break; //stop looping if correct
            } else {
                System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        // Loop until valid password
        while (true) {
            System.out.print("Enter Password: ");
            password = input.nextLine();
            if (checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured");
                break; //stop looping if correct
            } else {
                System.out.println("Password is not correctly formatted; please ensure it has at least 8 characters, a capital letter, a number, and a special character.");
            }
        }

        // Loop until valid cell phone number
        while (true) {
            System.out.print("Enter cellnumber: ");
            cellnumber = input.nextLine();
            if (checkCellPhoneNumber(cellnumber)) {
                System.out.println("Cell phone number successfully added");
                break; //stop looping if correct 
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }

        // When all details are correct, print registration complete message
        System.out.println("\n====================");
        System.out.println("REGISTRATION COMPLETE");
        System.out.println("====================");
        System.out.println("Username successfully captured");
        System.out.println("Password successfully captured");
        System.out.println("Cell phone number successfully added");

        return ""; // needs to return something
    }
    
    public boolean loginUser() {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your username to login: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password to login: ");
        String loginPassword = input.nextLine();
        //looping until login detail match whats stored
        return loginUsername.equals(username) && loginPassword.equals(password);
           
    }
    
    //helper function for testing to help pass login credentials
    public boolean loginUser(String username, String password) {
    return this.username.equals(username) && this.password.equals(password);
    }
    
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + firstname + " " + lastname + ". It is great to see you again.";
        } else {
            return "Username or password incorrect, please try again!";}
    }
}

    