package com.us.clique.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConnectAndRejectModle {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class Datum {

        @SerializedName("follower_user_name")
        @Expose
        private String followerUserName;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("follower_user_id")
        @Expose
        private String followerUserId;
        @SerializedName("following_status")
        @Expose
        private String followingStatus;
        @SerializedName("follower_profilePic")
        @Expose
        private String followerProfilePic;
        @SerializedName("follower_user_pic")
        @Expose
        private String followerUserPic;

        public String getFollowerUserName() {
            return followerUserName;
        }

        public void setFollowerUserName(String followerUserName) {
            this.followerUserName = followerUserName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFollowerUserId() {
            return followerUserId;
        }

        public void setFollowerUserId(String followerUserId) {
            this.followerUserId = followerUserId;
        }

        public String getFollowingStatus() {
            return followingStatus;
        }

        public void setFollowingStatus(String followingStatus) {
            this.followingStatus = followingStatus;
        }

        public String getFollowerProfilePic() {
            return followerProfilePic;
        }

        public void setFollowerProfilePic(String followerProfilePic) {
            this.followerProfilePic = followerProfilePic;
        }

        public String getFollowerUserPic() {
            return followerUserPic;
        }

        public void setFollowerUserPic(String followerUserPic) {
            this.followerUserPic = followerUserPic;
        }

    }}
