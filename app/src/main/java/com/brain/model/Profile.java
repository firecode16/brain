package com.brain.model;

import java.io.Serializable;

public class Profile implements Serializable {
    private long userId;
    private String userName;
    private String fullName;
    private String email;
    private ImageBinary backdropImage;
    private Integer countContacts;
    private boolean auth;

    public Profile() {
    }

    public Profile(long userId, String userName, String fullName, String email, ImageBinary backdropImage, Integer countContacts, boolean auth) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.backdropImage = backdropImage;
        this.countContacts = countContacts;
        this.auth = auth;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ImageBinary getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(ImageBinary backdropImage) {
        this.backdropImage = backdropImage;
    }

    public Integer getCountContacts() {
        return countContacts;
    }

    public void setCountContacts(Integer countContacts) {
        this.countContacts = countContacts;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}
