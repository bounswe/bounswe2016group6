package com.group6boun451.learner.model;

import com.group6boun451.learner.activity.HomePage;

import java.util.Date;

public class Comment {
    private Long id;
    private String content;
    private User owner;
    private Topic relatedTopic;
    private Date createdAt;

    public Comment() {
        super();
    }
    // constructor
    public Comment( long id1){
        this.id = id1;
    }

    public Comment(String content){
        this.content= content;
        this.owner = HomePage.user;
        this.createdAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Topic getRelatedTopic() {
        return relatedTopic;
    }

    public void setRelatedTopic(Topic relatedTopic) {
        this.relatedTopic = relatedTopic;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
