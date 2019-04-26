package com.gi2.servicedecovoiturage;


public class UserProfile {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;


    public UserProfile() {
        //firebase constructor
    }


    public UserProfile(String username, String password, String email, String phoneNumber) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
