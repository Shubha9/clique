package com.us.clique.bottomNavigation.modle;

public class ChatsModle {
    String name,message,date,notification,type,Status;

    public ChatsModle(String name, String message, String date, String notification,String status) {
        this.name = name;
        this.message = message;
        this.date = date;
        this.notification = notification;
        Status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
