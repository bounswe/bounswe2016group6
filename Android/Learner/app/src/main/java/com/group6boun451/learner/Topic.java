package com.group6boun451.learner;

import java.util.List;

/**
 * Created by muaz on 07.10.2016.
 */
public class Topic {
    private String text;
    private String title;
    private int imgid;
    private String editor;
    private String date;
    private int id;
    private CommentContainer comments;

    public Topic(int id){
        this.id = id;
    }



    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public int getImage() {
        return imgid;
    }

    public void setImage(int imgid) {
        this.imgid = imgid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public List<Comment> getComments() {
        return comments.getComments();
    }

    public void setComments(CommentContainer comments) {
        this.comments = comments;
    }
}
