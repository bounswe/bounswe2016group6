package org.learner.persistence.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table (name = "tag")

public class Tag {
	@JsonInclude(Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    private String context;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @JsonBackReference(value="relatedTopics")
	@ManyToMany(mappedBy = "tags")
	private List<Topic> relatedTopics;
	
	
    public List<Topic> getRelatedTopics() {
		return relatedTopics;
	}


	public void setRelatedTopics(List<Topic> relatedTopics) {
		this.relatedTopics = relatedTopics;
	}


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




    
    
}
