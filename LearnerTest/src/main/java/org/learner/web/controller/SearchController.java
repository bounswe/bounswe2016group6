package org.learner.web.controller;

import java.util.List;

import org.learner.persistence.model.Comment;
import org.learner.persistence.model.JViews;
import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@Controller

public class SearchController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
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
	
	@JsonView(JViews.Minimal.class)
	@RequestMapping("/search/suggest")
	@ResponseBody
	public List<Topic> searchSuggest(@RequestParam String query){
		LOGGER.debug("Topic Suggest!");
		
		List<Topic> suggestions = topicService.searchSuggest(query);
		
		LOGGER.debug("Suggestions : " + suggestions);
		return suggestions;
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
