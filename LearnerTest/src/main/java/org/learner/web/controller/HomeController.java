package org.learner.web.controller;

import java.security.Principal;
import java.util.List;

import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
import org.learner.service.ITopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ITopicService topicService;
	
    @RequestMapping(value="/home")
    public String homepage(Model model){
    	List<Topic> topicList = topicService.getRecentTopics();
    	
    	final Authentication curAuth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser =  curAuth.getName();
    	LOGGER.debug("USERNAME :" + currentUser);
    	LOGGER.debug("DETAILS :" + curAuth.getDetails());
    	
    	model.addAttribute("username", currentUser);
    	model.addAttribute("topics", topicList);
    	return "home";
    }
    
    @RequestMapping(value="/logout")
    public String logoutRedir(){
    	LOGGER.debug("Logout");
    	return "redirect:/login";
    }
    
    @RequestMapping(value="/")
    public String redirectToPage(final Principal principal){
    	LOGGER.debug("Context root!");
    	if(principal == null) {
    		LOGGER.debug("Principal : "+ principal );
    		return "redirect:/login";
    	}
    	
    	return "redirect:/home";
    }
}
