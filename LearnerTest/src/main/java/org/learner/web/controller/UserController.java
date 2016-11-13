package org.learner.web.controller;

import java.security.Principal;
import java.util.Locale;

import org.learner.persistence.model.User;
import org.learner.security.ActiveUserStore;
import org.learner.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
