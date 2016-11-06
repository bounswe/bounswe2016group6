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
    
    
    //TODO reveal > current date constraint
    private Date revealDate;
    
    
    
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
	
	
}
