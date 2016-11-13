package org.learner.persistence.dao;

import org.learner.persistence.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long>{

}
