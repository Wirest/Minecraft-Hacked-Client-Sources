
package com.youtube.playlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelatedPlaylists {

    @SerializedName("uploads")
    @Expose
    private String uploads;
    @SerializedName("watchHistory")
    @Expose
    private String watchHistory;
    @SerializedName("watchLater")
    @Expose
    private String watchLater;

    public String getUploads() {
        return uploads;
    }

    public void setUploads(String uploads) {
        this.uploads = uploads;
    }

    public String getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(String watchHistory) {
        this.watchHistory = watchHistory;
    }

    public String getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(String watchLater) {
        this.watchLater = watchLater;
    }

}
