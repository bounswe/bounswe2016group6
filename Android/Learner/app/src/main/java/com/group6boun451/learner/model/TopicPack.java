package com.group6boun451.learner.model;

import java.util.List;

/**
 * Created by Ahmet Zorer on 12/13/2016.
 */
public class TopicPack {
    private Long id;
    private String name;
    private List<Topic> topicList;

    public TopicPack(){}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Topic> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<Topic> topicList) {
        this.topicList = topicList;
    }
}
