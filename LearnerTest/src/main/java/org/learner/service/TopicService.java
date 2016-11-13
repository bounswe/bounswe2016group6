package org.learner.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.learner.persistence.dao.CommentRepository;
import org.learner.persistence.dao.TopicRepository;
import org.learner.persistence.dao.UserRepository;
import org.learner.persistence.model.Comment;
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
	CommentRepository commentRepository;
	
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
		
		//final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        //User currentUser = (User) curAuth.getPrincipal();
        
		topic.setOwner(userRepo.findOne((long)1));
		
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
			return null;
		}
		
		liked.setLikes(liked.getLikes()+1);
		
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
	public Tag createTagToTopic(long tid) {
		// TODO Auto-generated method stub
		return null;
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
	
	
	
}
