
package com.youtube;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentDetails {

    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("videoPublishedAt")
    @Expose
    private String videoPublishedAt;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoPublishedAt() {
        return videoPublishedAt;
    }

    public void setVideoPublishedAt(String videoPublishedAt) {
        this.videoPublishedAt = videoPublishedAt;
    }

}
