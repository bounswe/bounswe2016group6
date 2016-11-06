package org.learner.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.learner.persistence.model.Topic;
import org.learner.registration.TopicEvent;
import org.learner.service.ITopicService;
import org.learner.service.IUserService;
import org.learner.service.TopicService;
import org.learner.web.dto.TopicDto;
import org.learner.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/topic")
public class TopicController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;
    
    @Autowired
    private ITopicService topicService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    @GetMapping(value= "/create")
    public String createTopicPage(){
    	System.out.println("Topic Creation Page! ++");
    	return "createTopic";
    }
    
    @RequestMapping(value = "/create", method=RequestMethod.POST )
    public Topic createTopic(@RequestParam String header, @Valid TopicDto topicdto, final HttpServletRequest request){
    	
    	
    	LOGGER.debug("Request greeting! ++ NORELOAD ++++1111");
    	topicdto.setRevealDate(new Date());
    	final Topic posted = topicService.createNewTopic(topicdto);
    	
    	eventPublisher.publishEvent(new TopicEvent(posted));
    	
    	return posted;
    	//return new GenericResponse("Topic Creation Succeeded!");
    }
    
    
    @GetMapping(value="/edit/{topicId}")
    public String editTopicPage(@PathVariable Long topicId,Model model){
    	System.out.println("Edit topic page! NEW NORELOAD");
    	Topic editing = topicService.getTopicById(topicId);
    	
    	if(editing == null) {
    		//TODO topic not found
    		LOGGER.debug("Topic not found!");
    		return "editTopic";
    	}
    	
    	model.addAttribute("topic", editing);
    	
    	return "editTopic";
    }
    
    @PostMapping(value="/edit/{topicId}")
    public Topic editTopic(@PathVariable Long topicId){
    	System.out.println("Edit topic request");
    	
    	topicService.getTopicById(topicId);
    	
    	return null;
    }
    
    @RequestMapping(value="/greetings")
    @ResponseBody
    public Topic greetings(){
		Topic t = topicService.getTopicById(1);
		
    	return t;
    	
    }
    
}
