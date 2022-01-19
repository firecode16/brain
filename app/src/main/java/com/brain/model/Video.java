package com.brain.model;

import java.io.Serializable;

public class Video implements Serializable {
    private Long id;
    private int videoPath;
    private int imagePlay;
    private String userName;
    private String email;
    private String descriptionFooter;
    private int visits;

    public Video(Long id, int videoPath, int imagePlay, String userName, String email, String descriptionFooter, int visits) {
        this.id = id;
        this.videoPath = videoPath;
        this.imagePlay = imagePlay;
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

    public int getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(int videoPath) {
        this.videoPath = videoPath;
    }

    public int getImagePlay() {
        return imagePlay;
    }

    public void setImagePlay(int imagePlay) {
        this.imagePlay = imagePlay;
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
