package com.brain.model;

import java.io.Serializable;

public class Video implements Serializable {
    private Long id;
    private String urlVideo;
    private String userName;
    private String email;
    private String descriptionFooter;
    private int visits;

    public Video(Long id, String urlVideo, String userName, String email, String descriptionFooter, int visits) {
        this.id = id;
        this.urlVideo = urlVideo;
        this.userName = userName;
        this.email = email;
        this.descriptionFooter = descriptionFooter;
        this.visits = visits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
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
