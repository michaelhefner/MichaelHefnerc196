package com.michaelhefner.michaelhefnerc196.model;

public class Assessment {
    private String mType;
    private String mName;
    private String mID;
    public Assessment(String type, String name) {
        setName(name);
        setType(type);
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        this.mID = ID;
    }
}
