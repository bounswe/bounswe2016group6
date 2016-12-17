package org.learner.web.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.learner.persistence.model.Topic;


public class TopicDto {
    @NotNull
    @Size(min = 1)
    private String header;

    @NotNull
    @Size(min = 1)
    private String content;
    
    private String imagePath;
    
    private String[] question;
    private String[] answerA;
    private String[] answerB;
    private String[] answerC;
    private Integer[] correct;
    private String[] explanation;
    
    private Long topicPack;
    private String topicPackName;
    
	//TODO reveal > current date constraint
    private Date revealDate;
    
    public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
    
    public void generateFromModel(Topic topic){
    	this.setHeader(topic.getHeader());
    	this.setContent(topic.getContent());
    	this.setRevealDate(topic.getRevealDate());
    }
    
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TopicDTO [header=").append(header).append("]");
        return builder.toString();
    }

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getRevealDate() {
		return revealDate;
	}

	public void setRevealDate(Date revealDate) {
		this.revealDate = revealDate;
	}


	public String[] getQuestion() {
		return question;
	}


	public void setQuestion(String[] question) {
		this.question = question;
	}


	public String[] getAnswerA() {
		return answerA;
	}


	public void setAnswerA(String[] answerA) {
		this.answerA = answerA;
	}


	public String[] getAnswerB() {
		return answerB;
	}


	public void setAnswerB(String[] answerB) {
		this.answerB = answerB;
	}


	public String[] getAnswerC() {
		return answerC;
	}


	public void setAnswerC(String[] answerC) {
		this.answerC = answerC;
	}


	public Integer[] getCorrect() {
		return correct;
	}


	public void setCorrect(Integer[] correct) {
		this.correct = correct;
	}


	public String[] getExplanation() {
		return explanation;
	}


	public void setExplanation(String[] explanation) {
		this.explanation = explanation;
	}


	public Long getTopicPack() {
		return topicPack;
	}


	public void setTopicPack(Long topicPack) {
		this.topicPack = topicPack;
	}


	public String getTopicPackName() {
		return topicPackName;
	}


	public void setTopicPackName(String topicPackName) {
		this.topicPackName = topicPackName;
	}
	
	
}
