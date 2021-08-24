package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StagingModle {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("signup_stage")
        @Expose
        private String signupStage;

        public String getSignupStage() {
            return signupStage;
        }

        public void setSignupStage(String signupStage) {
            this.signupStage = signupStage;
        }

    }
}
