package com.us.clique.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;



public class PeoplePojo implements Serializable {

    @SerializedName("User")
    @Expose
    private List<User> user = null;

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public class User implements Serializable {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gender")
        @Expose
        private Object gender;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("profile_image_path")
        @Expose
        private String profileImagePath;
        @SerializedName("profile_pic")
        @Expose
        private Object profilePic;
        @SerializedName("profile_pic2")
        @Expose
        private Object profilePic2;
        @SerializedName("profile_pic3")
        @Expose
        private Object profilePic3;
        @SerializedName("profile_pic4")
        @Expose
        private Object profilePic4;
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
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("continent_name")
        @Expose
        private String continentName;
        @SerializedName("distance")
        @Expose
        private Double distance;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("image_no")
        @Expose
        private Integer image_no;
        @SerializedName("sub_cat")
        @Expose
        private List<SubCat> subCat = null;
        @SerializedName("login_user_sub_cat")
        @Expose
        private List<SubCat> loginUserSubCat = null;
        @SerializedName("Follower_Details")
        @Expose
        private List<FollowDetails> followDetails = null;
        @SerializedName("people_block_status")
        @Expose
        private String peopleblockstatus;
        @SerializedName("copy_pose_status")
        @Expose
        private Integer copyposestatus;

        public Integer getCopyposestatus() {
            return copyposestatus;
        }

        public void setCopyposestatus(Integer copyposestatus) {
            this.copyposestatus = copyposestatus;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getGender() {
            return gender;
        }

        public Integer getImage_no() {
            return image_no;
        }

        public void setImage_no(Integer image_no) {
            this.image_no = image_no;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Object getDob() {
            return dob;
        }

        public String getPeopleblockstatus() {
            return peopleblockstatus;
        }

        public void setPeopleblockstatus(String peopleblockstatus) {
            this.peopleblockstatus = peopleblockstatus;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public String getProfileImagePath() {
            return profileImagePath;
        }

        public void setProfileImagePath(String profileImagePath) {
            this.profileImagePath = profileImagePath;
        }

        public Object getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(Object profilePic) {
            this.profilePic = profilePic;
        }

        public Object getProfilePic2() {
            return profilePic2;
        }

        public void setProfilePic2(Object profilePic2) {
            this.profilePic2 = profilePic2;
        }

        public Object getProfilePic3() {
            return profilePic3;
        }

        public void setProfilePic3(Object profilePic3) {
            this.profilePic3 = profilePic3;
        }

        public Object getProfilePic4() {
            return profilePic4;
        }

        public void setProfilePic4(Object profilePic4) {
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

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getContinentName() {
            return continentName;
        }

        public void setContinentName(String continentName) {
            this.continentName = continentName;
        }

        public Double getDistance() {
            return distance;
        }
        public void setDistance(Double distance) {
            this.distance = distance;
        }


        public Integer getAge() {
            return age;
        }
        public void setAge(Integer age) {
            this.age = age;
        }

        public List<FollowDetails> getFollowDetails() {
            return followDetails;
        }

        public void setFollowDetails(List<FollowDetails> followDetails) {
            this.followDetails = followDetails;
        }

        public List<SubCat> getSubCat() {
            return subCat;
        }

        public void setSubCat(List<SubCat> subCat) {
            this.subCat = subCat;
        }

        public List<SubCat> getLoginUserSubCat() {
            return loginUserSubCat;
        }

        public void setLoginUserSubCat(List<SubCat> loginUserSubCat) {
            this.loginUserSubCat = loginUserSubCat;
        }

    }


    public class SubCat implements Serializable {

        @SerializedName("sk_sub_category_id")
        @Expose
        private String skSubCategoryId;
        @SerializedName("sub_category_name")
        @Expose
        private String subCategoryName;
        @SerializedName("sub_category_status")
        @Expose
        private String subCategoryStatus;
        @SerializedName("common")
        @Expose
        private Boolean common=false;

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

        public Boolean getCommon() {
            return common;
        }

        public void setCommon(Boolean common) {
            this.common = common;
        }
    }
    public class FollowDetails {

        @SerializedName("sk_follow_id")
        @Expose
        private String skFollowId;
        @SerializedName("fuser_id")
        @Expose
        private String fuserId;
        @SerializedName("follower_user_id")
        @Expose
        private String followerUserId;
        @SerializedName("following_status")
        @Expose
        private String followingStatus;
        @SerializedName("follow_status")
        @Expose
        private String followStatus;

        public String getSkFollowId() {
            return skFollowId;
        }

        public void setSkFollowId(String skFollowId) {
            this.skFollowId = skFollowId;
        }

        public String getFuserId() {
            return fuserId;
        }

        public void setFuserId(String fuserId) {
            this.fuserId = fuserId;
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

        public String getFollowStatus() {
            return followStatus;
        }

        public void setFollowStatus(String followStatus) {
            this.followStatus = followStatus;
        }

    }
}

