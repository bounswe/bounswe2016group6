package org.learner.service;

import java.util.Collection;
import java.util.List;

import org.learner.persistence.model.Comment;
import org.learner.persistence.model.Question;
import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
import org.learner.web.dto.TopicDto;

public interface ITopicService {
	
	Topic setTopicImage(long tid,String imgpath);
	
	Topic createNewTopic(TopicDto topicdto);
	
	void deleteTopic(long id);
	
	Topic updateTopic(long id,TopicDto topicdto);
	
	Topic getTopicById(long id);
	
	Topic getTopicByHeader(String hdr);

	Topic likeTopic(long topicId);

	List<Topic> getAllTopics();
	
	List<Topic> getRecentTopics();
	
	List<Topic> getRecommendedTopics();
	
	Comment createComment(long topicid,String content);
	
	void deleteComment(long topicid);
	
	Question createQuestions(Topic topic, TopicDto questions);
	
	//Question deleteQuestion(long id)
	
	//TAG_____
	Tag createTag(Tag tag);
	List<Tag> createTags(List<Tag> tags);
	
	Tag createTagToTopic(Topic topic,Tag tag);
	List<Tag> createTagsToTopic(Topic topic,List<Tag> tags);
	
	List<Tag> tagSuggest(String q);
	
	List<Topic> basicKeywordSearch(String keyword);
	
	List<Topic> getRelatedTopicsViaTopics(Topic topic);
	
	List<Topic> getRelatedTopicsViaTags(long tagid);
	
	List<Topic> getTopicsCreatedByUser();
	
	List<Topic> getTopicsLikedByUser();
	
	List<Topic> getTopicsCommentedByUser();

	Topic unlikeTopic(long topicId);
	
	List<Topic> searchSuggest(String q);

	List<Topic> getPopularTopics();

	List<Topic> getTopicsWithCommonTag(Topic topic);

	boolean addQuestionsToTopic(Topic topic, List<Question> questions);

	boolean removeQuestion(Long questionId);

	List<Topic> semanticSearch(String q);

	List<Topic> getOtherTopicsInPack(Topic topic);

	List<Topic> latestTopicsFromFollowing(List<User> teachers);
	
	
}
