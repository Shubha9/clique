package com.us.clique.modle;

public class MessagesModel {
    String message,userid,imageUrl,videoUrl;
    long timestamp;

    public MessagesModel() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public MessagesModel(String message, String userid, String imageUrl, String videoUrl, long timestamp) {
        this.message = message;
        this.userid = userid;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.timestamp = timestamp;
    }
}
