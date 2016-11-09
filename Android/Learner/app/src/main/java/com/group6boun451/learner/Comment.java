package com.group6boun451.learner;

/**
 * Created by muaz on 08.10.2016.
 */
public class Comment {
    //fields of comment
    private String userName;
    private String text;
    private String date;
    private int id;
    private int usrImgId;

    // constructor
    public Comment( int id1){

        this.id = id1;

    }

    //getter and setter

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsrImgId() {
        return usrImgId;
    }

    public void setUsrImgId(int usrImgId) {
        this.usrImgId = usrImgId;
    }
}
