package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.TopicPack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicPackRepository extends JpaRepository<TopicPack, Long>{
	public List<TopicPack> findByNameContaining(String searchterm);
}
