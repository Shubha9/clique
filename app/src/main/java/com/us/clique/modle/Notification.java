package com.us.clique.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("sk_setting_id")
    @Expose
    private String skSettingId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("pause_all")
    @Expose
    private String pauseAll;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("event_notify")
    @Expose
    private String eventNotify;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request")
    @Expose
    private String request;
    @SerializedName("email_sms")
    @Expose
    private String emailSms;
    @SerializedName("setting_status")
    @Expose
    private String settingStatus;

    public String getSkSettingId() {
        return skSettingId;
    }

    public void setSkSettingId(String skSettingId) {
        this.skSettingId = skSettingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPauseAll() {
        return pauseAll;
    }

    public void setPauseAll(String pauseAll) {
        this.pauseAll = pauseAll;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEventNotify() {
        return eventNotify;
    }

    public void setEventNotify(String eventNotify) {
        this.eventNotify = eventNotify;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getEmailSms() {
        return emailSms;
    }

    public void setEmailSms(String emailSms) {
        this.emailSms = emailSms;
    }

    public String getSettingStatus() {
        return settingStatus;
    }

    public void setSettingStatus(String settingStatus) {
        this.settingStatus = settingStatus;
    }
}
