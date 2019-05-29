package com.gi2.servicedecovoiturage;

public class Interaction {

    String driver;
    String client;
    String offer;
    String invitation;
    String state;
    String id;
    String clientPhoneNumber;
    String driverPhoneNumber;
    String type; // invitation or reservation

    public Interaction(){

    }

    public Interaction(String driver, String client, String offer, String state){
        this.driver=driver;
        this.client=client;
        this.offer=offer;
        this.state=state;
    }

    public String getClient() {
        return client;
    }

    public String getDriver() {
        return driver;
    }

    public String getOffer() {
        return offer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
