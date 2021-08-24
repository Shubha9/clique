package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatingBodyResponceModle {

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

        @SerializedName("sk_chat_id")
        @Expose
        private String skChatId;
        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("reciever_id")
        @Expose
        private String recieverId;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("chat_file")
        @Expose
        private String chatFile;
        @SerializedName("chat_date")
        @Expose
        private String chatDate;
        @SerializedName("chat_time")
        @Expose
        private String chatTime;
        @SerializedName("chat_status")
        @Expose
        private String chatStatus;
        @SerializedName("chat_file_path")
        @Expose
        private String chatFilePath;
        @SerializedName("UserData")
        @Expose
        private List<Object> userData = null;
        @SerializedName("ReceiverUserData")
        @Expose
        private ReceiverUserData receiverUserData;
        @SerializedName("SenderUserData")
        @Expose
        private SenderUserData senderUserData;

        public String getSkChatId() {
            return skChatId;
        }

        public void setSkChatId(String skChatId) {
            this.skChatId = skChatId;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getRecieverId() {
            return recieverId;
        }

        public void setRecieverId(String recieverId) {
            this.recieverId = recieverId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getChatFile() {
            return chatFile;
        }

        public void setChatFile(String chatFile) {
            this.chatFile = chatFile;
        }

        public String getChatDate() {
            return chatDate;
        }

        public void setChatDate(String chatDate) {
            this.chatDate = chatDate;
        }

        public String getChatTime() {
            return chatTime;
        }

        public void setChatTime(String chatTime) {
            this.chatTime = chatTime;
        }

        public String getChatStatus() {
            return chatStatus;
        }

        public void setChatStatus(String chatStatus) {
            this.chatStatus = chatStatus;
        }

        public String getChatFilePath() {
            return chatFilePath;
        }

        public void setChatFilePath(String chatFilePath) {
            this.chatFilePath = chatFilePath;
        }

        public List<Object> getUserData() {
            return userData;
        }

        public void setUserData(List<Object> userData) {
            this.userData = userData;
        }

        public ReceiverUserData getReceiverUserData() {
            return receiverUserData;
        }

        public void setReceiverUserData(ReceiverUserData receiverUserData) {
            this.receiverUserData = receiverUserData;
        }

        public SenderUserData getSenderUserData() {
            return senderUserData;
        }

        public void setSenderUserData(SenderUserData senderUserData) {
            this.senderUserData = senderUserData;
        }

    }
    public class ReceiverUserData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

    }
    public class SenderUserData {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

    }
}
