package com.michaelhefner.michaelhefnerc196.model;

public class Assessment {
    private String mType;
    private String mName;
    private String mStartDate;
    private String mEndDate;
    private String mID;
    public Assessment(String type, String name, String startDate, String endDate) {
        setName(name);
        setType(type);
        setStart(startDate);
        setEnd(endDate);
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

    public String getStart() {
        return mStartDate;
    }

    public void setStart(String start) {
        this.mStartDate = start;
    }

    public String getEnd() {
        return mEndDate;
    }

    public void setEnd(String end) {
        this.mEndDate = end;
    }
}
