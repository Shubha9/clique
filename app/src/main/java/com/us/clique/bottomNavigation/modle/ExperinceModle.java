package com.us.clique.bottomNavigation.modle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ExperinceModle {

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
    public class Datum implements Serializable
    {

        @SerializedName("sk_event_id")
        @Expose
        private String skEventId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("event_image")
        @Expose
        private String eventImage;
        @SerializedName("event_image_path")
        @Expose
        private String eventImagePath;
        @SerializedName("event_image_pic")
        @Expose
        private String eventImagePic;
        @SerializedName("minimum_member")
        @Expose
        private String minimumMember;
        @SerializedName("maximum_member")
        @Expose
        private String maximumMember;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("tags")
        @Expose
        private String tags;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("event_type")
        @Expose
        private String eventType;
        @SerializedName("master_event_id")
        @Expose
        private Object masterEventId;
        @SerializedName("event_date")
        @Expose
        private String eventDate;
        @SerializedName("event_time")
        @Expose
        private String eventTime;
        @SerializedName("today")
        @Expose
        private String today;
        @SerializedName("event_date_format")
        @Expose
        private String eventDateFormat;
        @SerializedName("event_time_format")
        @Expose
        private String eventTimeFormat;
        @SerializedName("event_status")
        @Expose
        private String eventStatus;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("continent_name")
        @Expose
        private String continentName;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_dob")
        @Expose
        private String userDob;
        @SerializedName("profile_pic")
        @Expose
        private String profilePic;
        @SerializedName("profile_pic_path")
        @Expose
        private String profilePicPath;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("event_user_id")
        @Expose
        private String eventUserId;
        @SerializedName("event_url")
        @Expose
        private String eventUrl;
        @SerializedName("profile_image2")
        @Expose
        private String profileImage2;
        @SerializedName("ClonedUsers")
        @Expose
        private List<Object> clonedUsers = null;
        @SerializedName("age")
        @Expose
        private Integer age;
        @SerializedName("ReportCount")
        @Expose
        private String reportCount;
        @SerializedName("NotInterestedCount")
        @Expose
        private String notInterestedCount;
        @SerializedName("SaveCount")
        @Expose
        private String saveCount;
        @SerializedName("BlockCount")
        @Expose
        private String blockCount;
        @SerializedName("RequestCount")
        @Expose
        private String requestCount;
        @SerializedName("MessageRequestCount")
        @Expose
        private String messageRequestCount;
        @SerializedName("ParticipantsCount")
        @Expose
        private String participantsCount;
        @SerializedName("MessageAcceptedsCount")
        @Expose
        private String messageAcceptedsCount;
        @SerializedName("tag_list")
        @Expose
        private List<String> tagList = null;
        private final static long serialVersionUID = -3655547142412016030L;

        public String getSkEventId() {
            return skEventId;
        }

        public void setSkEventId(String skEventId) {
            this.skEventId = skEventId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getEventImage() {
            return eventImage;
        }

        public void setEventImage(String eventImage) {
            this.eventImage = eventImage;
        }

        public String getEventImagePath() {
            return eventImagePath;
        }

        public void setEventImagePath(String eventImagePath) {
            this.eventImagePath = eventImagePath;
        }

        public String getEventImagePic() {
            return eventImagePic;
        }

        public void setEventImagePic(String eventImagePic) {
            this.eventImagePic = eventImagePic;
        }

        public String getMinimumMember() {
            return minimumMember;
        }

        public void setMinimumMember(String minimumMember) {
            this.minimumMember = minimumMember;
        }

        public String getMaximumMember() {
            return maximumMember;
        }

        public void setMaximumMember(String maximumMember) {
            this.maximumMember = maximumMember;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Object getMasterEventId() {
            return masterEventId;
        }

        public void setMasterEventId(Object masterEventId) {
            this.masterEventId = masterEventId;
        }

        public String getEventDate() {
            return eventDate;
        }

        public void setEventDate(String eventDate) {
            this.eventDate = eventDate;
        }

        public String getEventTime() {
            return eventTime;
        }

        public void setEventTime(String eventTime) {
            this.eventTime = eventTime;
        }

        public String getToday() {
            return today;
        }

        public void setToday(String today) {
            this.today = today;
        }

        public String getEventDateFormat() {
            return eventDateFormat;
        }

        public void setEventDateFormat(String eventDateFormat) {
            this.eventDateFormat = eventDateFormat;
        }

        public String getEventTimeFormat() {
            return eventTimeFormat;
        }

        public void setEventTimeFormat(String eventTimeFormat) {
            this.eventTimeFormat = eventTimeFormat;
        }

        public String getEventStatus() {
            return eventStatus;
        }

        public void setEventStatus(String eventStatus) {
            this.eventStatus = eventStatus;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getContinentName() {
            return continentName;
        }

        public void setContinentName(String continentName) {
            this.continentName = continentName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserDob() {
            return userDob;
        }

        public void setUserDob(String userDob) {
            this.userDob = userDob;
        }

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getProfilePicPath() {
            return profilePicPath;
        }

        public void setProfilePicPath(String profilePicPath) {
            this.profilePicPath = profilePicPath;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getEventUserId() {
            return eventUserId;
        }

        public void setEventUserId(String eventUserId) {
            this.eventUserId = eventUserId;
        }

        public String getEventUrl() {
            return eventUrl;
        }

        public void setEventUrl(String eventUrl) {
            this.eventUrl = eventUrl;
        }

        public String getProfileImage2() {
            return profileImage2;
        }

        public void setProfileImage2(String profileImage2) {
            this.profileImage2 = profileImage2;
        }

        public List<Object> getClonedUsers() {
            return clonedUsers;
        }

        public void setClonedUsers(List<Object> clonedUsers) {
            this.clonedUsers = clonedUsers;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getReportCount() {
            return reportCount;
        }

        public void setReportCount(String reportCount) {
            this.reportCount = reportCount;
        }

        public String getNotInterestedCount() {
            return notInterestedCount;
        }

        public void setNotInterestedCount(String notInterestedCount) {
            this.notInterestedCount = notInterestedCount;
        }

        public String getSaveCount() {
            return saveCount;
        }

        public void setSaveCount(String saveCount) {
            this.saveCount = saveCount;
        }

        public String getBlockCount() {
            return blockCount;
        }

        public void setBlockCount(String blockCount) {
            this.blockCount = blockCount;
        }

        public String getRequestCount() {
            return requestCount;
        }

        public void setRequestCount(String requestCount) {
            this.requestCount = requestCount;
        }

        public String getMessageRequestCount() {
            return messageRequestCount;
        }

        public void setMessageRequestCount(String messageRequestCount) {
            this.messageRequestCount = messageRequestCount;
        }

        public String getParticipantsCount() {
            return participantsCount;
        }

        public void setParticipantsCount(String participantsCount) {
            this.participantsCount = participantsCount;
        }

        public String getMessageAcceptedsCount() {
            return messageAcceptedsCount;
        }

        public void setMessageAcceptedsCount(String messageAcceptedsCount) {
            this.messageAcceptedsCount = messageAcceptedsCount;
        }

        public List<String> getTagList() {
            return tagList;
        }

        public void setTagList(List<String> tagList) {
            this.tagList = tagList;
        }

    }
}
