package org.learner.web.controller;

import org.learner.persistence.model.Comment;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/topic/comment")
public class CommentController {
	@Autowired
	ITopicService topicService;
	
	
	
	@RequestMapping(value = "/create")
	@ResponseBody
	public GenericResponse createTopic(@RequestParam Long topicId,@RequestParam String content){
		
		
		Comment comment = topicService.createComment(topicId, content);
		
		if(comment == null){
			return new GenericResponse("Comment creation failed");
		}
		
		return new GenericResponse("Comment creation successful");
		
	}
}
