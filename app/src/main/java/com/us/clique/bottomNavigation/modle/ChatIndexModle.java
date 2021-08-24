package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatIndexModle {

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

        @SerializedName("unique_request_id")
        @Expose
        private String uniqueRequestId;
        @SerializedName("recieverId")
        @Expose
        private String recieverId;
        @SerializedName("recieverName")
        @Expose
        private String recieverName;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("profile_path_pic")
        @Expose
        private String profilePathPic;
        @SerializedName("online_status")
        @Expose
        private Integer onlineStatus;
        @SerializedName("minutes")
        @Expose
        private String minutes;
        @SerializedName("unreadMsgCount")
        @Expose
        private String unreadMsgCount;
        @SerializedName("latestchat_time")
        @Expose
        private String latestchatTime;
        @SerializedName("latestchat_date")
        @Expose
        private String latestchatDate;
        @SerializedName("latestMessage")
        @Expose
        private String latestMessage;

        public String getUniqueRequestId() {
            return uniqueRequestId;
        }

        public void setUniqueRequestId(String uniqueRequestId) {
            this.uniqueRequestId = uniqueRequestId;
        }

        public String getRecieverId() {
            return recieverId;
        }

        public void setRecieverId(String recieverId) {
            this.recieverId = recieverId;
        }

        public String getRecieverName() {
            return recieverName;
        }

        public void setRecieverName(String recieverName) {
            this.recieverName = recieverName;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getProfilePathPic() {
            return profilePathPic;
        }

        public void setProfilePathPic(String profilePathPic) {
            this.profilePathPic = profilePathPic;
        }

        public Integer getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(Integer onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getMinutes() {
            return minutes;
        }

        public void setMinutes(String minutes) {
            this.minutes = minutes;
        }

        public String getUnreadMsgCount() {
            return unreadMsgCount;
        }

        public void setUnreadMsgCount(String unreadMsgCount) {
            this.unreadMsgCount = unreadMsgCount;
        }

        public String getLatestchatTime() {
            return latestchatTime;
        }

        public void setLatestchatTime(String latestchatTime) {
            this.latestchatTime = latestchatTime;
        }

        public String getLatestchatDate() {
            return latestchatDate;
        }

        public void setLatestchatDate(String latestchatDate) {
            this.latestchatDate = latestchatDate;
        }

        public String getLatestMessage() {
            return latestMessage;
        }

        public void setLatestMessage(String latestMessage) {
            this.latestMessage = latestMessage;
        }

    }
}
