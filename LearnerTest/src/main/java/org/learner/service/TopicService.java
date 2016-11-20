package org.learner.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.learner.persistence.dao.CommentRepository;
import org.learner.persistence.dao.QuestionRepository;
import org.learner.persistence.dao.TagRepository;
import org.learner.persistence.dao.TopicRepository;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.model.Comment;
import org.learner.persistence.model.Question;
import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
import org.learner.web.dto.TopicDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class TopicService implements ITopicService{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TopicRepository repository;
	
	@Autowired 
	TagRepository tagRepo;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	QuestionRepository questionRepo;
	
	@Override
	public Topic setTopicImage(long tid, String imgpath) {
		Topic edit = repository.findOne(tid);
		edit.setHeaderImage(imgpath);
		return edit;
	}

	@Override
	public Topic createNewTopic(TopicDto topicdto) {
		
		if((new Date()).before( topicdto.getRevealDate() ) ){
			//TODO reveal date exception
		}
		

		final Topic topic = new Topic();
		
		topic.setHeader(topicdto.getHeader());
		topic.setContent(topicdto.getContent());
		topic.setRevealDate(topicdto.getRevealDate());
		topic.setCreatedAt(new Date());
		
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        
        User owner = userRepo.findByEmail(currentUserName);
        topic.setOwner(owner);
		
        return repository.save(topic);
	}

	@Override
	public void deleteTopic(long id) {
		
	}

	@Override
	public Topic updateTopic(long id, TopicDto topicdto) {
		
		Topic edit = repository.findOne(id);
		if(edit == null){
			return null;
		}
		edit.setContent(topicdto.getContent());
		edit.setHeader(topicdto.getHeader());
		repository.save(edit);
		//TODO insert tags
		return edit;
	}

	@Override
	public Topic getTopicById(long id) {
		return repository.findOne(id);
	}

	@Override
	public Topic getTopicByHeader(String hdr) {
		
		return null;
	}

	@Override
	public Topic likeTopic(long topicId) {
		Topic liked = repository.findOne(topicId);
		if(liked == null){
			System.out.println("topic not found!");
			return null;
		}
		
		User user = getCurrentUser();
		System.out.println("User :" + user);
		
		if(!liked.getLikedBy().contains(user)){
			liked.getLikedBy().add(user);
			System.out.println("Like save!");
		} else {
			System.out.println("Already liked!");
			return null;
		}
		
		//TODO insert tags
		return liked;
	}
	
	@Override
	public Topic unlikeTopic(long topicId) {
		Topic liked = repository.findOne(topicId);
		if(liked == null){
			System.out.println("topic not found!");
			return null;
		}
		
		User user = getCurrentUser();
		System.out.println("User :" + user);
		
		liked.getLikedBy().remove(user);
		
		//TODO insert tags
		return liked;
	}
	
	
	@Override
	public List<Topic> getAllTopics() {
		return repository.findAll();
		
	}

	@Override
	public Comment createComment(long id, String content) {
		
		Comment cmnt = new Comment();
		cmnt.setContent(content);
		
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        
        User owner = userRepo.findByEmail(currentUserName);
        cmnt.setOwner(owner);
        
        Topic tpc = repository.findOne(id);
        cmnt.setRelatedTopic(tpc);
		
        cmnt.setCreatedAt(new Date());
		commentRepository.save(cmnt);
		return cmnt;
	}

	@Override
	public void deleteComment(long id) {
		commentRepository.delete(id);
	}

	@Override
	public List<Topic> getRecentTopics() {
		return repository.findTop3ByOrderByCreatedAtDesc();
	}

	@Override
	public List<Topic> getRecommendedTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Tag createTagToTopic(Topic topic,Tag tag) {
		topic.getTags().add(tag);
		return tag;
	}

	@Override
	public void deleteTagFromQuestion(long tid, long tagid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Topic> basicKeywordSearch(String keyword) {
		
		return repository.findByHeaderContaining(keyword);
	}

	@Override
	public List<Topic> getRelatedTopicsViaTopics(long topicId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getRelatedTopicsViaTags(long tagid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsCreatedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsLikedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsCommentedByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> getTopicsFromFollowedTeacher() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLikeCount(long tid) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	private User getCurrentUser(){
		final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = curAuth.getName();
        
        User owner = userRepo.findByEmail(currentUserName);
        return owner;
	}
	
	
	
	@Override
	public List<Tag> tagSuggest(String q) {
		
		return tagRepo.findByNameContaining(q);
	}

	@Override
	public Tag createTag(Tag tag) {
		Tag created = tagRepo.save(tag);
		
		return created;
	}

	@Override
	public List<Tag> createTags(List<Tag> tags) {
		List<Tag> created = tagRepo.save(tags);
		return created;
	}

	@Override
	public List<Tag> createTagsToTopic(Topic topic, List<Tag> tags) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Topic> searchSuggest(String q) {
		List<Topic> topicSuggest = repository.findTop10ByHeaderContainingOrderByCreatedAtDesc(q);
		return topicSuggest;
	}

	@Override
	public Question createQuestions(Topic topic, TopicDto questions) {
		List<Question> ques = new ArrayList<>();
		for(int i = 0; i<questions.getQuestion().length ;i++){
			Question q = new Question();
			q.setQuestion(questions.getQuestion()[i]);
			q.setAnswerA(questions.getAnswerA()[i]);
			q.setAnswerB(questions.getAnswerB()[i]);
			q.setAnswerC(questions.getAnswerC()[i]);
			q.setCorrect(questions.getCorrect()[i]);
			q.setCorrect(questions.getCorrect()[i]);
			q.setExplanation(questions.getExplanation()[i]);
			q.setRelatedTopic(topic);
			ques.add(q);
		}
		if(ques.isEmpty()){
			return null;
		}
		questionRepo.save(ques);
		return ques.get(0);
	}
	
	
}
