package com.brain.model;

import java.io.Serializable;

public class Profile implements Serializable {
    private long userId;
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String registrationDate;
    private boolean auth;

    public Profile() { }

    public Profile(long userId, String userName, String fullName, String email, String phone, String registrationDate, boolean auth) {
        this.userId = userId;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.auth = auth;
    }

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public boolean isAuth() {
        return auth;
    }
}
