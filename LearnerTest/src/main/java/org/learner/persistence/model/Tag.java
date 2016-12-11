package org.learner.persistence.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table (name = "tag")

public class Tag {
	@JsonIgnoreProperties(ignoreUnknown=true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String name;
    
    private String context;
    
    @JsonIgnore
    @ElementCollection
    private Set<String> conceptRelations;
    
    @Column(name = "created_at")
    private Date createdAt;
    
    @JsonBackReference(value="relatedTopics")
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "tags")
	private List<Topic> relatedTopics;
	
    
    @Transient
	int searchPoint = 0;
    
    public void incrementSearchPoint(int inc) {
		this.searchPoint += inc;
	}
    
    public int getSearchPoint() {
		return searchPoint;
	}


	public void setSearchPoint(int searchPoint) {
		this.searchPoint = searchPoint;
	}


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


	public Set<String> getConceptRelations() {
		return conceptRelations;
	}


	public void setConceptRelations(Set<String> conceptRelations) {
		this.conceptRelations = conceptRelations;
	}


	


    
    
}
