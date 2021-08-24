package com.us.clique.landingScreen;
public class frameitems {
    private int img;
    private String title, skip;
    private  String description;
    public frameitems(int img,String title,String description,String skip) {
        this.img=img;
        this.title=title;
        this.description=description;
        this.skip=skip;
    }

    public frameitems() {

    }

    public static void add(frameitems frameitems) {
    }
    public int getImg(int frame1){
        return img;
    }
    public void setImg(int img){
        this.img=img;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getDescription(){
        return description;

    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getSkip(){
        return skip;
    }
    public void setSkip(String skip){
        this.skip=skip;
    }
}