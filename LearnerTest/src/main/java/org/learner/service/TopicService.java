package org.learner.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.learner.persistence.dao.TopicRepository;
import org.learner.persistence.dao.UserRepository;
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
	public Topic deleteTopic() {
		// TODO Auto-generated method stub
		return null;
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
	
	
	
}
