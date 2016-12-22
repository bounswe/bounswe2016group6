package com.group6boun451.learner.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Model class for tags.
 */
public class Tag {
    private Long id;
    private String name;
    private String context;
    private Set<String> conceptRelations;
    private Date createdAt;
    private List<Topic> relatedTopics;
    private int searchPoint = 0;

    public Tag(){}
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


    public String getContext() {
        return context;
    }


    public void setContext(String context) {
        this.context = context;
    }


    public Date getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public List<Topic> getRelatedTopics() {
        return relatedTopics;
    }

    public void setRelatedTopics(List<Topic> relatedTopics) {
        this.relatedTopics = relatedTopics;
    }

    public Set<String> getConceptRelations() {
        return conceptRelations;
    }

    public void setConceptRelations(Set<String> conceptRelations) {
        this.conceptRelations = conceptRelations;
    }

    public int getSearchPoint() {
        return searchPoint;
    }

    public void setSearchPoint(int searchPoint) {
        this.searchPoint = searchPoint;
    }
}
