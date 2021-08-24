package com.us.clique.signIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaceBookLogin {
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

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private Object gender;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("profile_pic")
        @Expose
        private Object profilePic;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("bio_data")
        @Expose
        private Object bioData;
        @SerializedName("user_interest")
        @Expose
        private Object userInterest;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("Accesstoken")
        @Expose
        private String accesstoken;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public Object getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(Object profilePic) {
            this.profilePic = profilePic;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Object getBioData() {
            return bioData;
        }

        public void setBioData(Object bioData) {
            this.bioData = bioData;
        }

        public Object getUserInterest() {
            return userInterest;
        }

        public void setUserInterest(Object userInterest) {
            this.userInterest = userInterest;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getAccesstoken() {
            return accesstoken;
        }

        public void setAccesstoken(String accesstoken) {
            this.accesstoken = accesstoken;
        }

    }
}
