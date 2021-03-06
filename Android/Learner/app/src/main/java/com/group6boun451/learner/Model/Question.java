package com.group6boun451.learner.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Model class for questions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements Serializable {
    private Long id;
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private int correct;
    private String explanation;

    private Topic relatedTopic;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public Topic getRelatedTopic() {
        return relatedTopic;
    }

    public void setRelatedTopic(Topic relatedTopic) {
        this.relatedTopic = relatedTopic;
    }


    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
