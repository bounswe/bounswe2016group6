package org.learner.persistence.dao;

import java.util.List;

import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicRepository extends JpaRepository<Topic, Long> {
	Topic findByHeader(String header);
    
	public List<Topic> findTop3ByOrderByCreatedAtDesc();
	public List<Topic> findTop5ByOrderByCreatedAtDesc();
	public List<Topic> findTop7ByOrderByCreatedAtDesc();
	
	public List<Topic> findTop9ByOrderByCreatedAtDesc();
	
	
	public List<Topic> findByHeaderContaining(String hdr);
	
	public List<Topic> findTop10ByHeaderContainingOrderByCreatedAtDesc(String hdr);
	
	
	@Query( "select distinct tp from Topic tp "
			+ " join tp.tags tg "
			+ " where tg in :source and tp.id != :origid"
			)
	public List<Topic> topicsWithCommonTags(@Param("source") List<Tag> source,
											@Param("origid") Long origid);
	
	
	public List<Topic> findByTagsIn(List<Tag> thetags);
	
    @Override
    void delete(Topic topic);

}
