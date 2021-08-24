package com.us.clique.bottomNavigation.modle;

public class MoveModle {
    String movieName,time,address,people,type;

    public MoveModle(String movieName, String time, String address, String people,String type) {
        this.movieName = movieName;
        this.time = time;
        this.address = address;
        this.people = people;
        this.type = type ;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTime() {
        return time;
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

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }
}
