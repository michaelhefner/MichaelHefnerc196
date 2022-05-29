package com.michaelhefner.michaelhefnerc196.model;

public class Instructor {
    private String mName;
    private String mPhoneNumber;
    private String mEmailAddress;
    private String mID;

    public Instructor(String name, String phoneNumber, String emailAddress){
        setEmailAddress(emailAddress);
        setName(name);
        setPhoneNumber(phoneNumber);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        this.mID = ID;
    }
}
