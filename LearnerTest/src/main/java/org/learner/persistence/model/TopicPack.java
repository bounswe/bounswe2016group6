package org.learner.persistence.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class TopicPack {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String name;
	
	@JsonBackReference
	@OneToMany(mappedBy = "topicPack" , fetch = FetchType.LAZY)
	private List<Topic> topicList;
	
	
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
