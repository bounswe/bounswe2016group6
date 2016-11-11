package org.learner.service;

import java.util.Collection;
import java.util.List;

import org.learner.persistence.model.Topic;
import org.learner.web.dto.TopicDto;

public interface ITopicService {
	
	Topic createNewTopic(TopicDto topicdto);
	
	Topic deleteTopic();
	
	Topic updateTopic(long id,TopicDto topicdto);
	
	Topic getTopicById(long id);
	
	Topic getTopicByHeader(String hdr);

	Topic likeTopic(long topicId);

	List<Topic> getAllTopics();
	
}
