package com.brain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class Result {
    @SerializedName("posterId")
    @Expose
    private Long posterId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("releaseDate")
    @Expose
    private String releaseDate;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("posterPath")
    @Expose
    private List<MediaDetail> posterPath = new ArrayList<>();

    public Long getPosterId() {
        return posterId;
    }

    public void setPosterId(Long posterId) {
        this.posterId = posterId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<MediaDetail> getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(List<MediaDetail> posterPath) {
        this.posterPath = posterPath;
    }
}
