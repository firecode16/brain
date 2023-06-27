package com.brain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class MediaApiResponse implements Serializable {
    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("data")
    @Expose
    private Result data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }
}
