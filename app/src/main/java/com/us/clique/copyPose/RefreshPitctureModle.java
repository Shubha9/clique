package com.us.clique.copyPose;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RefreshPitctureModle {

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
    public class Datum {

        @SerializedName("pic_id")
        @Expose
        private String picId;
        @SerializedName("pic_name")
        @Expose
        private String picName;
        @SerializedName("pic_image_path")
        @Expose
        private String picImagePath;
        @SerializedName("pic_image")
        @Expose
        private String picImage;
        @SerializedName("pic_status")
        @Expose
        private String picStatus;

        public String getPicId() {
            return picId;
        }

        public void setPicId(String picId) {
            this.picId = picId;
        }

        public String getPicName() {
            return picName;
        }

        public void setPicName(String picName) {
            this.picName = picName;
        }

        public String getPicImagePath() {
            return picImagePath;
        }

        public void setPicImagePath(String picImagePath) {
            this.picImagePath = picImagePath;
        }

        public String getPicImage() {
            return picImage;
        }

        public void setPicImage(String picImage) {
            this.picImage = picImage;
        }

        public String getPicStatus() {
            return picStatus;
        }

        public void setPicStatus(String picStatus) {
            this.picStatus = picStatus;
        }

    }
}
