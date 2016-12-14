package org.learner.persistence.dao;

import org.learner.persistence.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
	
}
