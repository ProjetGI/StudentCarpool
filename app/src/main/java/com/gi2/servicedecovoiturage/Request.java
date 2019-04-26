package com.gi2.servicedecovoiturage;

public class Request {
    private String client;
    private String departure;
    private String arrival;
    private String time;
    private String date;
    private String documentName;


    public Request(){
    }

    public Request(String client, String departure, String arrival, String time, String date){
        this.client = client;
        this.departure = departure;
        this.arrival = arrival;
        this.time = time;
        this.date = date;
    }
    public String getClient(){ return client;}
    public String getDeparture(){ return departure;}
    public String getArrival(){ return arrival;}
    public String getTime(){ return time;}
    public String getDate(){ return time;}

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }



}
