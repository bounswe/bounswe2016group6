package org.learner.web.controller;

import java.util.List;

import org.learner.persistence.model.Comment;
import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class SearchController {
	@Autowired
	ITopicService topicService;
	
	
	
	@RequestMapping(value = "/search/keyword")
	@ResponseBody
	public List<Topic> basicSearch(@RequestParam String keyword){
		System.out.println("HELLOWORLD");
		if(keyword == null){
			return null;
		}
		
		return topicService.basicKeywordSearch(keyword);
		
	}
	
	@RequestMapping(value = "/search")
	public String basicSearchResults(@RequestParam String q,Model model){
		System.out.println("HELLOWORLD");
		if(q == null){
			return null;
		}
		
		List<Topic> queryResults = topicService.basicKeywordSearch(q);
		model.addAttribute("topics", queryResults);
		return "searchresult";
		
	}
}
