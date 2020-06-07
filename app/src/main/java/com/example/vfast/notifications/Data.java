package com.example.vfast.notifications;

public class Data {

    private String user,body,title,sent,type;

    public Data(){
    }

    public Data(String user, String body, String title, String sent, String type) {
        this.user = user;
        this.body = body;
        this.title = title;
        this.sent = sent;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}
