package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProfileModle implements Serializable {

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

        @SerializedName("User")
        @Expose
        private List<User> user = null;

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

    }

    public class User implements Serializable {


        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("profile_image_path")
        @Expose
        private String profileImagePath;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("profile_pic2")
        @Expose
        private String profilePic2;
        @SerializedName("profile_pic3")
        @Expose
        private String profilePic3;
        @SerializedName("profile_pic4")
        @Expose
        private String profilePic4;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("profile_image2")
        @Expose
        private String profileImage2;
        @SerializedName("profile_image3")
        @Expose
        private String profileImage3;
        @SerializedName("profile_image4")
        @Expose
        private String profileImage4;
        @SerializedName("mobile")
        @Expose
        private Object mobile;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("bio_data")
        @Expose
        private String bioData;
        @SerializedName("user_interest")
        @Expose
        private String userInterest;
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("PeopleCount")
        @Expose
        private String peopleCount;
        @SerializedName("Follower_Details")
        @Expose
        private List<Object> followerDetails = null;
        @SerializedName("sub_cat")
        @Expose
        private List<SubCat> subCat = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getProfileImagePath() {
            return profileImagePath;
        }

        public void setProfileImagePath(String profileImagePath) {
            this.profileImagePath = profileImagePath;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getProfilePic2() {
            return profilePic2;
        }

        public void setProfilePic2(String profilePic2) {
            this.profilePic2 = profilePic2;
        }

        public String getProfilePic3() {
            return profilePic3;
        }

        public void setProfilePic3(String profilePic3) {
            this.profilePic3 = profilePic3;
        }

        public String getProfilePic4() {
            return profilePic4;
        }

        public void setProfilePic4(String profilePic4) {
            this.profilePic4 = profilePic4;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getProfileImage2() {
            return profileImage2;
        }

        public void setProfileImage2(String profileImage2) {
            this.profileImage2 = profileImage2;
        }

        public String getProfileImage3() {
            return profileImage3;
        }

        public void setProfileImage3(String profileImage3) {
            this.profileImage3 = profileImage3;
        }

        public String getProfileImage4() {
            return profileImage4;
        }

        public void setProfileImage4(String profileImage4) {
            this.profileImage4 = profileImage4;
        }

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBioData() {
            return bioData;
        }

        public void setBioData(String bioData) {
            this.bioData = bioData;
        }

        public String getUserInterest() {
            return userInterest;
        }

        public void setUserInterest(String userInterest) {
            this.userInterest = userInterest;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(String peopleCount) {
            this.peopleCount = peopleCount;
        }

        public List<Object> getFollowerDetails() {
            return followerDetails;
        }

        public void setFollowerDetails(List<Object> followerDetails) {
            this.followerDetails = followerDetails;
        }

        public List<SubCat> getSubCat() {
            return subCat;
        }

        public void setSubCat(List<SubCat> subCat) {
            this.subCat = subCat;
        }

    }
    public class SubCat implements Serializable{

        @SerializedName("sk_sub_category_id")
        @Expose
        private String skSubCategoryId;
        @SerializedName("sub_category_name")
        @Expose
        private String subCategoryName;
        @SerializedName("sub_category_status")
        @Expose
        private String subCategoryStatus;

        public String getSkSubCategoryId() {
            return skSubCategoryId;
        }

        public void setSkSubCategoryId(String skSubCategoryId) {
            this.skSubCategoryId = skSubCategoryId;
        }

        public String getSubCategoryName() {
            return subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        public String getSubCategoryStatus() {
            return subCategoryStatus;
        }

        public void setSubCategoryStatus(String subCategoryStatus) {
            this.subCategoryStatus = subCategoryStatus;
        }

    }
}
