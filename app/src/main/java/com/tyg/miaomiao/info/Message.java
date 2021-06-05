package com.tyg.miaomiao.info;

public class Message {
    private int headImg;
    private String username;
    private String content;
    private String time;

    public Message(int handImage, String name, String text, String date) {
        headImg = handImage;
        username = name;
        content = text;
        time = date;
    }

    public int getHeadImg() {
        return headImg;
    }

    public void setHeadImg(int headImg) {
        this.headImg = headImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
