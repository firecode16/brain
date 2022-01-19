package com.brain.model;

import java.io.Serializable;

public class Poster implements Serializable {
    private Long id;
    private String userName;
    private String email;
    private int image;
    private String descriptionFooter;
    private int visits;

    public Poster() {}

    public Poster(Long id, String userName, String email, int image, String descriptionFooter, int visits) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.image = image;
        this.descriptionFooter = descriptionFooter;
        this.visits = visits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
