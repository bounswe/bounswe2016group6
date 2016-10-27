package org.learner.persistence.dao;

import org.learner.persistence.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findByHeader(String header);
    
    
    @Override
    void delete(Topic topic);

}
