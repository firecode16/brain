package com.brain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class Result implements Serializable {
    @SerializedName("userId")
    @Expose
    private Long userId;

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("fullName")
    @Expose
    private String fullName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("backdropName")
    @Expose
    private String backdropName;

    @SerializedName("countContacts")
    @Expose
    private Integer countContacts;

    @SerializedName("post")
    @Expose
    private List<MediaDetail> post = new ArrayList<>();

    @SerializedName("auth")
    @Expose
    private boolean auth;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getBackdropName() {
        return backdropName;
    }

    public void setBackdropName(String backdropName) {
        this.backdropName = backdropName;
    }

    public Integer getCountContacts() {
        return countContacts;
    }

    public void setCountContacts(Integer countContacts) {
        this.countContacts = countContacts;
    }

    public List<MediaDetail> getPost() {
        return post;
    }

    public void setPost(List<MediaDetail> post) {
        this.post = post;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}
