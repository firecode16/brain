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
public class MediaDetail implements Serializable {
    @SerializedName("collectionId")
    @Expose
    private Long collectionId;

    @SerializedName("postDate")
    @Expose
    private String postDate;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("array")
    @Expose
    private Integer array;

    @SerializedName("content")
    @Expose
    private List<MediaContent> content = new ArrayList<>();

    public Long getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Integer getArray() {
        return array;
    }

    public void setArray(Integer array) {
        this.array = array;
    }

    public List<MediaContent> getContent() {
        return content;
    }

    public void setContent(List<MediaContent> content) {
        this.content = content;
    }
}
