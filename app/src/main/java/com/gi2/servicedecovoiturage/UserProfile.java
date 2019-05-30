package com.gi2.servicedecovoiturage;


public class UserProfile {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String CNE;
    private Boolean admin;
    private Boolean pending;
    private String userid;



    public UserProfile() {
        //firebase constructor
    }

    public String getUserid() {
        return userid;
    }
    public Boolean getPending() {
        return pending;
    }
    public void setPending(Boolean pend) {
        this.pending=pend;
    }
    public void setUserid(String id) {
        this.userid = id;
    }

    public UserProfile(String username, String password, String email, String phoneNumber,String CNE, Boolean type,String id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.CNE = CNE;
        this.admin = type;
        this.userid=id;
        this.pending=true;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }


    public Boolean isAdmin() {
        return admin;
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

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
