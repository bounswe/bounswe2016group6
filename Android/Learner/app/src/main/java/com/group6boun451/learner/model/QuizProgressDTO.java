package com.group6boun451.learner.model;

/**
 * Created by Ahmet Zorer on 12/18/2016.
 */

import java.util.ArrayList;
import java.util.List;
public class QuizProgressDto {
    TopicPack topicPack;
    List<QuizResult> results = new ArrayList<>();
    List<Topic> remaining = new ArrayList<>();
    int packTotal = 0;
    int packCompleted = 0;

    public TopicPack getTopicPack() {
        return topicPack;
    }
    public void setTopicPack(TopicPack topicPack) {
        this.topicPack = topicPack;
    }
    public List<QuizResult> getResults() {
        return results;
    }
    public void setResults(List<QuizResult> results) {
        this.results = results;
    }
    public List<Topic> getRemaining() {
        return remaining;
    }
    public void setRemaining(List<Topic> remaining) {
        this.remaining = remaining;
    }
    public int getPackTotal() {
        return packTotal;
    }
    public void setPackTotal(int packTotal) {
        this.packTotal = packTotal;
    }
    public int getPackCompleted() {
        return packCompleted;
    }
    public void setPackCompleted(int packCompleted) {
        this.packCompleted = packCompleted;
    }
}