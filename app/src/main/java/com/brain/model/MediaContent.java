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
public class MediaContent implements Serializable {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("collectionId")
    @Expose
    private Long collectionId;

    @SerializedName("contentType")
    @Expose
    private String contentType;

    @SerializedName("multimediaName")
    @Expose
    private String multimediaName;

    @SerializedName("share")
    @Expose
    private Integer share;

    @SerializedName("likes")
    @Expose
    private Integer likes;

    @SerializedName("comments")
    @Expose
    private List<String> comments = new ArrayList<>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMultimediaName() {
        return multimediaName;
    }

    public void setMultimediaName(String multimediaName) {
        this.multimediaName = multimediaName;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
