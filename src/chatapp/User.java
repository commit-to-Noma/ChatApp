/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package chatapp;

import javax.swing.*;

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
        // First name
        firstname = JOptionPane.showInputDialog(null, "Enter First Name:", "👤 Registration", JOptionPane.QUESTION_MESSAGE);
        if (firstname == null) return "Cancelled";

        // Last name
        lastname = JOptionPane.showInputDialog(null, "Enter Last Name:", "👤 Registration", JOptionPane.QUESTION_MESSAGE);
        if (lastname == null) return "Cancelled";

        // Username loop
        while (true) {
            username = JOptionPane.showInputDialog(null, "Enter Username (must include '_' and max 5 characters):", "🆔 Username", JOptionPane.QUESTION_MESSAGE);
            if (username == null) return "Cancelled";
            if (CheckUserName(username)) {
                JOptionPane.showMessageDialog(null, "✅ Username successfully captured");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "❌ Invalid username.\nIt must contain an underscore and be no more than 5 characters.");
            }
        }

        // Password loop
        while (true) {
            password = JOptionPane.showInputDialog(null, "Enter Password (min 8 chars, 1 capital, 1 digit, 1 special):", "🔐 Password", JOptionPane.QUESTION_MESSAGE);
            if (password == null) return "Cancelled";
            if (checkPasswordComplexity(password)) {
                JOptionPane.showMessageDialog(null, "✅ Password successfully captured");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "❌ Invalid password.\nMust be at least 8 characters long with uppercase, digit, and special character.");
            }
        }

        // Cell number loop
        while (true) {
            cellnumber = JOptionPane.showInputDialog(null, "Enter Cell Number (e.g. +27831234567):", "📱 Phone Number", JOptionPane.QUESTION_MESSAGE);
            if (cellnumber == null) return "Cancelled";
            if (checkCellPhoneNumber(cellnumber)) {
                JOptionPane.showMessageDialog(null, "📱 Cell phone number successfully added");
                break;
            } else {
                JOptionPane.showMessageDialog(null, "❌ Invalid number.\nMust start with '+' and include correct digits.");
            }
        }

        // Final confirmation
        JOptionPane.showMessageDialog(null,
                "🎉 REGISTRATION COMPLETE\n\n" +
                        "✅ Username captured\n" +
                        "✅ Password captured\n" +
                        "📱 Cell phone number added",
                "✅ Registration Success",
                JOptionPane.INFORMATION_MESSAGE);

        return "Success";
    }
    
    public boolean loginUser() {
        String loginUsername = JOptionPane.showInputDialog(null, "Enter your username:", "🔐 Login", JOptionPane.QUESTION_MESSAGE);
        if (loginUsername == null) return false;

        String loginPassword = JOptionPane.showInputDialog(null, "Enter your password:", "🔐 Login", JOptionPane.QUESTION_MESSAGE);
        if (loginPassword == null) return false;

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

    