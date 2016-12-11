package org.learner.web.controller;

import java.util.List;

import org.learner.persistence.model.Question;
import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/quiz")
public class QuizController {
	@Autowired
	ITopicService topicService;
	
	
	@RequestMapping(value = "/create/{topicId}")
	@ResponseBody
	public GenericResponse createTopic(@PathVariable Long topicId,@RequestBody List<Question> questions){
		Topic topic = topicService.getTopicById(topicId);
		if(topic == null) {
			return new GenericResponse(null, "TopicID not found!");
		}
		topicService.addQuestionsToTopic(topic,questions);
		return new GenericResponse("Questions creation successful!");
	}
	
	@RequestMapping(value = "/remove/{questionId}")
	@ResponseBody
	public GenericResponse createTopic(@PathVariable Long questionId){
		
		
		boolean result = topicService.removeQuestion(questionId);
		
		if(!result) return new GenericResponse("","Questions removal failed!");
		return new GenericResponse("Questions creation successful!");
		
	}
	
}
