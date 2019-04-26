package com.gi2.servicedecovoiturage;

public class Voyage {
    private String mDriver;
    private String mDeparture;
    private String mArrival;
    private String mPrice;
    private String mTime;
    private String mDate;
    private String documentName;

    public Voyage (){
    }

    public Voyage(String mDriver,String mDeparture,String mArrival,String mPrice,String mTime,String mDate){
        this.mDriver = mDriver;
        this.mDeparture = mDeparture;
        this.mArrival = mArrival;
        this.mPrice = mPrice;
        this.mTime = mTime;
        this.mDate = mDate;
    }

    public String getmDriver(){ return mDriver;}
    public String getmDeparture(){ return mDeparture;}
    public String getmArrival(){ return mArrival;}
    public String getmPrice(){ return mPrice;}
    public String getmTime(){ return mTime;}
    public String getmDate(){ return mDate;}

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
