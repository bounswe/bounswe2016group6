package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>{

	public List<Tag> findByNameContaining(String searchterm);
	
	public List<Tag> findByContextContaining(String st);
	
	
}
