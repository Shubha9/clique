package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestsResponceModle {
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

        @SerializedName("requested_user_name")
        @Expose
        private String requestedUserName;
        @SerializedName("requested_user_pic")
        @Expose
        private String requestedUserPic;
        @SerializedName("event_title")
        @Expose
        private String eventTitle;
        @SerializedName("request_type")
        @Expose
        private String requestType;
        @SerializedName("request_user_id")
        @Expose
        private String requestUserId;
        @SerializedName("event_id")
        @Expose
        private String eventId;
        @SerializedName("event_user_id")
        @Expose
        private String eventUserId;
        @SerializedName("requesting_status")
        @Expose
        private String requestingStatus;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("created_time")
        @Expose
        private String createdTime;

        public String getRequestedUserName() {
            return requestedUserName;
        }

        public void setRequestedUserName(String requestedUserName) {
            this.requestedUserName = requestedUserName;
        }

        public String getRequestedUserPic() {
            return requestedUserPic;
        }

        public void setRequestedUserPic(String requestedUserPic) {
            this.requestedUserPic = requestedUserPic;
        }

        public String getEventTitle() {
            return eventTitle;
        }

        public void setEventTitle(String eventTitle) {
            this.eventTitle = eventTitle;
        }

        public String getRequestType() {
            return requestType;
        }

        public void setRequestType(String requestType) {
            this.requestType = requestType;
        }

        public String getRequestUserId() {
            return requestUserId;
        }

        public void setRequestUserId(String requestUserId) {
            this.requestUserId = requestUserId;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getEventUserId() {
            return eventUserId;
        }

        public void setEventUserId(String eventUserId) {
            this.eventUserId = eventUserId;
        }

        public String getRequestingStatus() {
            return requestingStatus;
        }

        public void setRequestingStatus(String requestingStatus) {
            this.requestingStatus = requestingStatus;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

    }
}
