package org.learner.web.controller;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.learner.persistence.model.Topic;
import org.learner.registration.TopicEvent;
import org.learner.service.ITopicService;
import org.learner.service.IUserService;
import org.learner.storage.StorageException;
import org.learner.storage.StorageService;
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
import org.springframework.web.multipart.MultipartFile;

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
    
    private StorageService storageService;

    @Autowired
    public TopicController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @GetMapping(value= "/create")
    public String createTopicPage(){
    	System.out.println("Topic Creation Page! ++");
    	return "createTopic";
    }
    
    
    @RequestMapping(value = "/imageUpload")
    @ResponseBody
    public GenericResponse uploadImage(@RequestParam MultipartFile image,HttpServletRequest request){
    	System.out.println("Image upload request!");
    	System.out.println(image);
    	storageService.store(image);
    	System.out.println("Store complete!");
    	
    	return new GenericResponse("Upload successful!");
    }
    
    @RequestMapping(value="/create/android")
    @ResponseBody
    public GenericResponse createTopicWithoutImage(@Valid TopicDto topicdto,final HttpServletRequest request){
    	LOGGER.debug("Topic creation for Android!");
    	topicdto.setRevealDate(new Date());
    	topicdto.setImagePath("/images/topic");
    	final Topic posted = topicService.createNewTopic(topicdto);
    	
    	if(posted == null){
    		return new GenericResponse("Topic creation failed!","Db error!");
    	}
    	
    	LOGGER.debug(posted.toString());
    	return new GenericResponse("Topic Creation Succeeded!");
    	
    }
    
    @RequestMapping(value = "/create", method=RequestMethod.POST )
    @ResponseBody
    public GenericResponse createTopic(@RequestParam String header, 
    								@Valid TopicDto topicdto, 
    								@RequestParam MultipartFile image,
    								final HttpServletRequest request){
    	
    	LOGGER.debug("Request greeting! ++ NORELOAD ++++1111");
    	
    	topicdto.setRevealDate(new Date());
    	topicdto.setImagePath("/images/topic");
    	final Topic posted = topicService.createNewTopic(topicdto);
    	
    	if(posted == null){
    		return new GenericResponse("Topic creation failed!","Db error!");
    	}
    	String imgname = "tim" + posted.getId() + ".jpg";
    	
    	if(image != null){
        	try{
        		storageService.store(image,imgname);
        	} catch (StorageException e) {
        		return new GenericResponse("Topic creation failed!","Image upload failed!");
    		}
        	
        	topicService.setTopicImage(posted.getId(), "/images/topic/"+imgname);
    		
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
    @ResponseBody
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
    @ResponseBody
    public Topic viewTopic(@PathVariable Long topicId){
    	LOGGER.debug("Topic view!");
    	//Topic topic = 
    	return topicService.getTopicById(topicId);
    }
    
    
    
    @RequestMapping(value="/recent")
    @ResponseBody
    public Collection<Topic> recentTopics(){
    	
    	return topicService.getRecentTopics();
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
