package com.us.clique.adapter;

public class RemoveParticipant {
    String name;
    int img;
    public RemoveParticipant(int img,String name) {
        this.img = img;
        this.name = name;
    }
    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
