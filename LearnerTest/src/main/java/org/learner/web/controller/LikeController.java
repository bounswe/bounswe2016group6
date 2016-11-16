package org.learner.web.controller;

import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {
	@Autowired
	ITopicService topicService;
	
	
	@RequestMapping(value="/topic/like/{topicId}")
	@ResponseBody
	public GenericResponse topicLike(@PathVariable Long topicId){
		
		Topic topic = topicService.likeTopic(topicId);
		
		if(topic == null) {
			return new GenericResponse("like failed!","Fail!");
		}
		return new GenericResponse("Liked!");
	}
	
	@RequestMapping(value="/topic/unlike/{topicId}")
	@ResponseBody
	public GenericResponse topicUnlike(@PathVariable Long topicId){
		
		Topic topic = topicService.unlikeTopic(topicId);
		
		return new GenericResponse("Unliked!");
	}
}
