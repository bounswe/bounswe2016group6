package org.learner.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public GenericResponse createTopic(@RequestParam String header, @Valid TopicDto topicdto, final HttpServletRequest request){
    	
    	
    	LOGGER.debug("Request greeting! ++ NORELOAD ++++1111");
    	topicdto.setRevealDate(new Date());
    	final Topic posted = topicService.createNewTopic(topicdto);
    	
    	if(posted == null){
    		return new GenericResponse("Topic creation failed!");
    	}
    	eventPublisher.publishEvent(new TopicEvent(posted));
    	LOGGER.debug(posted.toString());
    	return new GenericResponse("Topic Creation Succeeded!");
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
    public GenericResponse editTopic(@PathVariable Long topicId,TopicDto topicdto){
    	System.out.println("Edit topic request");
    	
    	Topic edited = topicService.updateTopic(topicId, topicdto);
    	LOGGER.debug(edited.toString());
    	
    	return new GenericResponse("Topic update successful!");
    	
    }
    
    @PostMapping(value="/like/{topicId}")
    public GenericResponse likeTopic(@PathVariable Long topicId){
    	System.out.println("Edit topic request");
    	
    	Topic liked = topicService.likeTopic(topicId);
    	//LOGGER.debug(edited.toString());
    	
    	return new GenericResponse("Topic like successful!");
    	
    }
    
    @RequestMapping(value = "/view/{topicId}")
    public Topic viewTopic(@PathVariable Long topicId){
    	LOGGER.debug("Topic view!");
    	//Topic topic = 
    	return topicService.getTopicById(topicId);
    }
    
    
    
    @GetMapping(value="/yeter")
    @ResponseBody
    public Greeting yether(){
    	System.out.println("YETHER!");
    	Greeting g = new Greeting(1,"Hello Worasdasda");
    	
    	System.out.println(g.getContent());
    	
    	return g;
    }
    
    @RequestMapping(value="/recommended")
    @ResponseBody
    public Collection<Topic> topicList(){
    	return topicService.getAllTopics();
    }
    
    @RequestMapping(value="/{id}")
    public String viewTopicPage(@PathVariable Long id,Model model){
    	
    	Topic topic = topicService.getTopicById(id);
    	if(topic==null){
    		model.addAttribute("errormessage","Topic not found!");
    	}
    	
    	
    	model.addAttribute("topic", topic);
    	
    	return "viewTopic";
    	
    }
}
