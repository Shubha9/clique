package com.us.clique.modle;

import androidx.annotation.DrawableRes;

public class CalenderNestedModle {

    @DrawableRes
    final int image;

    String movieName,locatiion,viewer,type;

    public CalenderNestedModle(int image, String movieName, String locatiion, String viewer,String type) {
        this.image = image;
        this.movieName = movieName;
        this.locatiion = locatiion;
        this.viewer = viewer;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getLocatiion() {
        return locatiion;
    }

    public void setLocatiion(String locatiion) {
        this.locatiion = locatiion;
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }
}
