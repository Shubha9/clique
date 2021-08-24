package com.us.clique.modle;

public class YourExperienceModle {

    String moveiName,time,address,noOfPeople,description,type;

    public YourExperienceModle(String moveiName, String time, String address, String noOfPeople, String description, String type) {
        this.moveiName = moveiName;
        this.time = time;
        this.address = address;
        this.noOfPeople = noOfPeople;
        this.description = description;
        this.type = type;
    }

    public String getMoveiName() {
        return moveiName;
    }

    public void setMoveiName(String moveiName) {
        this.moveiName = moveiName;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
