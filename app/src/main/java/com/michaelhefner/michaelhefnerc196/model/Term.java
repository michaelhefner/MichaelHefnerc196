package com.michaelhefner.michaelhefnerc196.model;

public class Term {

    private String mTitle;
    private String mStartDate;
    private String mEndDate;
    private String mName;
    private String mID;

    public Term(String title, String start, String end, String name) {
        setTitle(title);
        setStart(start);
        setEnd(end);
        setName(name);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
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
