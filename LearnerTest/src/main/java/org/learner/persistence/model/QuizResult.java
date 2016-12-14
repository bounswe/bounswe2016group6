package org.learner.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "quiz_result")
public class QuizResult {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String title;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User solver;
	
	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic masterTopic;
	
	private int correct;
	
	private int questionCount;
	
	private Date takenAt;
	
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
