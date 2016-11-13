package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicRepository extends JpaRepository<Topic, Long> {
	Topic findByHeader(String header);
    
	public List<Topic> findTop3ByOrderByCreatedAtDesc();
    
    @Override
    void delete(Topic topic);

}
