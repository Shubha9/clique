package com.us.clique.modle;

import android.graphics.Bitmap;
import android.net.Uri;

public class ChatingBodyModle {
    String chating;
    Bitmap imgage;
    Uri selectedImage;

    public ChatingBodyModle(String chating, Bitmap imgage, Uri selectedImage) {
        this.chating = chating;
        this.imgage = imgage;
        this.selectedImage = selectedImage;
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getChating() {
        return chating;
    }

    public Bitmap getImgage() {
        return imgage;
    }

    public void setImgage(Bitmap imgage) {
        this.imgage = imgage;
    }

    public void setChating(String chating) {
        this.chating = chating;
    }
}
