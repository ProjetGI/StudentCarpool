package com.gi2.servicedecovoiturage;

public class Report {
    private String username;
    private  String reported_user;
    private String description;
    private String titre;

    public Report(String username, String reported_user, String description,String titre) {
        this.username = username;
        this.reported_user = reported_user;
        this.description = description;
        this.titre=titre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getUsername() {
        return username;
    }
    public Report(){

    }
    public String getReported_user() {
        return reported_user;
    }

    public String getDescription() {
        return description;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReported_user(String reported_user) {
        this.reported_user = reported_user;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
