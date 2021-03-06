package org.learner.web.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.learner.persistence.model.Topic;
import org.learner.persistence.model.User;
import org.learner.security.ActiveUserStore;
import org.learner.service.ITopicService;
import org.learner.service.IUserService;
import org.learner.web.dto.QuizProgressDto;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;
    
	@Autowired
	ITopicService topicService;
    
    @Autowired
    IUserService userService;
    
    @RequestMapping(value = "/loggedUsers", method = RequestMethod.GET)
    public String getLoggedUsers(final Locale locale, final Model model) {
        model.addAttribute("users", activeUserStore.getUsers());
        return "users";
    }
    
    @RequestMapping(value="/profile", method=RequestMethod.GET)
    public String profilePage(final Model model,final Principal principal){
    	String currentUsername = principal.getName();
    	User user = userService.findUserByEmail(currentUsername);
    	List<Topic> recommendedList = topicService.getRecommendedTopics();
    	List<Topic> latestFromTeacher = topicService.latestTopicsFromFollowing(user.getFollowing());
    	List<QuizProgressDto> qpdto = topicService.getQuizProgress();
    	model.addAttribute("latestFromFollowed", latestFromTeacher);
    	model.addAttribute("progress",qpdto);
    	model.addAttribute("recommended", recommendedList);
    	model.addAttribute("user", user);
    	return "profile";
    }
    
    @RequestMapping(value="/profile/{userId}")
    public String anotherProfilePage( @PathVariable Long userId ,final Model model){
    	User user = userService.getUserByID(userId);
    	List<Topic> latestFromTeacher = topicService.latestTopicsFromFollowing(user.getFollowing());

    	model.addAttribute("user", user);
    	model.addAttribute("latestFromFollowed", latestFromTeacher);
    	return "profile2";
    	
    }
    
    @RequestMapping(value="/userprofile")
    @ResponseBody
    public User userDetails(final Principal principal){
    	String currentUsername = principal.getName();
    	User user = userService.findUserByEmail(currentUsername);
    	
    	return user;
    }
    
    @RequestMapping(value="/userprofile/{userId}")
    @ResponseBody
    public User anotherUserDetails(@PathVariable Long userId){
    	User user = userService.getUserByID(userId);
    	return user;
    }
    
    @RequestMapping(value="/follow/{teacherId}")
    @ResponseBody
    public GenericResponse followTeacher(@PathVariable Long teacherId){
    	
    	User user = userService.followUser(teacherId);
    	
    	if(user == null){
    		return new GenericResponse("Follow operation failed!","Follow operation failed!");
    	}
    	
    	
    	return new GenericResponse("Teacher with id :" + user.getId() +" followed!");
    }
    
    @RequestMapping(value="/unfollow/{teacherId}")
    @ResponseBody
    public GenericResponse unfollowTeacher(@PathVariable Long teacherId){
    	
    	User user = userService.unfollowUser(teacherId);
    	if(user == null){
    		return new GenericResponse("UNFollow operation failed!","UNFollow operation failed!");
    	}
    	return new GenericResponse("Teacher with id :" + user.getId() +" unfollowed!");
    }
    
    @RequestMapping(value="/user/following")
    @ResponseBody
    public List<User> retrieveFollowing(final Principal principal){
    	String currentUsername = principal.getName();
    	User user = userService.findUserByEmail(currentUsername);
    	
    	
    	return user.getFollowing();
    }
    
    
    @RequestMapping(value = "/user/suggest")
    @ResponseBody
    public List<User> usernameSuggest(@RequestParam String q){
    	List<User> us = new ArrayList<>();
    	us = userService.usernameSuggest(q);
    	return us; 
    	
    }
    
}
