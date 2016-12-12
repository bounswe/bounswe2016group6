package org.learner.web.controller;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

import org.learner.persistence.model.User;
import org.learner.security.ActiveUserStore;
import org.learner.service.IUserService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;
    
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
    	
    	model.addAttribute("user", user);
    	return "profile";
    }
    
    @RequestMapping(value="/userprofile")
    @ResponseBody
    public User userDetails(final Principal principal){
    	String currentUsername = principal.getName();
    	User user = userService.findUserByEmail(currentUsername);
    	
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
    

    
}
