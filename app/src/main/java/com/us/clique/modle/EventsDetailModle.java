package com.us.clique.modle;

import androidx.annotation.DrawableRes;

public class EventsDetailModle {

    public EventsDetailModle(int images) {
        this.images = images;
    }

    @DrawableRes
    final int images;

    public int getImages() {
        return images;
    }
}
