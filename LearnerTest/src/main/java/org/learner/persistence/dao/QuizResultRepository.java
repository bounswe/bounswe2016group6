package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.QuizResult;
import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
	
	List<QuizResult> findBySolver(User user);

	QuizResult findByMasterTopicAndSolver(Topic masterTopic, User u);
	
}
