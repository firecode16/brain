package com.brain.model;

import java.io.Serializable;

public class Poster implements Serializable {
    private String id;
    private String userName;
    private String email;
    private String descriptionFooter;
    private int visits;

    public Poster() {
    }

    public Poster(String id, String userName, String email, String descriptionFooter, int visits) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.descriptionFooter = descriptionFooter;
        this.visits = visits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescriptionFooter() {
        return descriptionFooter;
    }

    public void setDescriptionFooter(String descriptionFooter) {
        this.descriptionFooter = descriptionFooter;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
