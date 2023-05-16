package com.brain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class MediaDetail {
    private Long id;
    @SerializedName("backdropPath")
    @Expose
    private String backdropPath;
    @SerializedName("like")
    @Expose
    private Long like;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("binaryContent")
    @Expose
    private ImageBinary binaryContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Long getLike() {
        return like;
    }

    public void setLike(Long like) {
        this.like = like;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ImageBinary getBinaryContent() {
        return binaryContent;
    }

    public void setBinaryContent(ImageBinary binaryContent) {
        this.binaryContent = binaryContent;
    }
}
