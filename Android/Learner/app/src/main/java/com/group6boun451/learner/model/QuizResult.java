package com.group6boun451.learner.model;

import java.util.Date;

/**
 * Created by Ahmet Zorer on 12/18/2016.
 */

public class QuizResult {
    private Long id;
    private String title;
    private User solver;
    private Topic masterTopic;
    private int correct;
    private int questionCount;
    private Date takenAt;

    public QuizResult(){
        title = "Topic Quiz.";
    }

    public QuizResult(int correct, int questionCount){
        title = "Topic Quiz.";
        setCorrect(correct);
        setQuestionCount(questionCount);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getSolver() {
        return solver;
    }

    public void setSolver(User solver) {
        this.solver = solver;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public Topic getMasterTopic() {
        return masterTopic;
    }

    public void setMasterTopic(Topic masterTopic) {
        this.masterTopic = masterTopic;
    }
}