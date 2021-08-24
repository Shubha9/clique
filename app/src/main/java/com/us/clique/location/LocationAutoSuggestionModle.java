package com.us.clique.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LocationAutoSuggestionModle implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    private final static long serialVersionUID = -3678706809979315087L;

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


    public class Datum implements Serializable {

        @SerializedName("sk_location_id")
        @Expose
        private String skLocationId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("location_status")
        @Expose
        private String locationStatus;
        @SerializedName("city_id")
        @Expose
        private String cityId;
        @SerializedName("city_name")
        @Expose
        private String cityName;
        @SerializedName("state_id")
        @Expose
        private String stateId;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("country_name")
        @Expose
        private String countryName;
        @SerializedName("continent_id")
        @Expose
        private String continentId;
        @SerializedName("continent_name")
        @Expose
        private String continentName;
        private final static long serialVersionUID = -3449852556449648835L;

        public String getSkLocationId() {
            return skLocationId;
        }

        public void setSkLocationId(String skLocationId) {
            this.skLocationId = skLocationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getLocationStatus() {
            return locationStatus;
        }

        public void setLocationStatus(String locationStatus) {
            this.locationStatus = locationStatus;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateId() {
            return stateId;
        }

        public void setStateId(String stateId) {
            this.stateId = stateId;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getContinentId() {
            return continentId;
        }

        public void setContinentId(String continentId) {
            this.continentId = continentId;
        }

        public String getContinentName() {
            return continentName;
        }

        public void setContinentName(String continentName) {
            this.continentName = continentName;
        }

    }
}
