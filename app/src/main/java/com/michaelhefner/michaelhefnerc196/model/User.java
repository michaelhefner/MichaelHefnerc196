package com.michaelhefner.michaelhefnerc196.model;

import java.util.List;

public class User {

    private String mName;
    private String mPassword;
    private String mUsername;
    private List<Term> mTerms;

    public User(String name, String password, String username, List<Term> terms){
        setName(name);
        setPassword(password);
        setUsername(username);
        setTerms(terms);
    }

    public List<Term> getTerms() {
        return mTerms;
    }

    public void setTerms(List<Term> terms) {
        mTerms = terms;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

}
