
/*
    5.  Include the following details for each course:
        •  The course title
        •  The start date
        •  The end date
        •  The status (in progress, completed, dropped, plan to take)
        •  The course instructors’ names, phone numbers, and e-mail addresses
 */
package com.michaelhefner.michaelhefnerc196.model;

import java.util.List;

public class Course {

    private String mTitle;
    private String mStartDate;
    private String mEndDate;
    private String mStatus;
    private String mTerm;
    private String mAssessment;

    public Course(String title, String startDate, String endDate, String status, String assessment, String term) {
        setTitle(title);
        setAssessment(assessment);
        setEndDate(endDate);
        setStartDate(startDate);
        setStatus(status);
        setTerm(term);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getAssessment() {
        return mAssessment;
    }

    public void setAssessment(String assessment) {
        this.mAssessment = assessment;
    }

    public String getTerm() {
        return mTerm;
    }

    public void setTerm(String mTerm) {
        this.mTerm = mTerm;
    }
}
