package com.gi2.servicedecovoiturage;

public class Voyage {
    private String mDriver;
    private String mDeparture;
    private String mArrival;
    private String mPrice;
    private String mTime;
    private String mDate;
    private String maxPlaces;
    private String currentPlaces;
    private String mDescription;
    private String documentName;

    public Voyage (){
    }

    public Voyage(String mDriver,String mDeparture,String mArrival,String mPrice,String mTime,String mDate,String mDescription,String maxPlaces,String currentPlaces){
        this.mDriver = mDriver;
        this.mDeparture = mDeparture;
        this.mArrival = mArrival;
        this.mPrice = mPrice;
        this.mTime = mTime;
        this.mDate = mDate;
        this.mDescription = mDescription;
        this.maxPlaces = maxPlaces;
        this.currentPlaces = currentPlaces;
    }

    public String getmDriver(){ return mDriver;}
    public String getmDeparture(){ return mDeparture;}
    public String getmArrival(){ return mArrival;}
    public String getmPrice(){ return mPrice;}
    public String getmTime(){ return mTime;}
    public String getmDate(){ return mDate;}


    public String getmDescription() {
        return mDescription;
    }

    public String getCurrentPlaces() {
        return currentPlaces;
    }

    public String getMaxPlaces() {
        return maxPlaces;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
