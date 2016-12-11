package org.learner.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.learner.persistence.dao.TagRepository;
import org.learner.persistence.model.Comment;
import org.learner.persistence.model.JViews;
import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

@Controller

public class SearchController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ITopicService topicService;
	
	@Autowired
	TagRepository tagRepo;
	
	
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
	
	@RequestMapping(value="/search/tag/{tagId}")
	public String tagSearchResults(@PathVariable Long tagId,Model model){
		List<Topic> relatedTopics = topicService.getRelatedTopicsViaTags(tagId);
		
		List<Topic> queryResults = relatedTopics == null ? new ArrayList<Topic>(): relatedTopics;
		model.addAttribute("topics", queryResults);
		return "searchresult";
	}
	
	@RequestMapping(value="/tag/search/{tagId}")
	@ResponseBody
	public List<Topic> tagRelatedTopics(@PathVariable Long tagId){
		List<Topic> relatedTopics = topicService.getRelatedTopicsViaTags(tagId);
		return relatedTopics;
	}
	
	@RequestMapping(value="/semanticSearch")
	@ResponseBody
	public List<Topic> advanced(@RequestParam String q){
		LOGGER.debug("Semantic Search");
		List<Topic> semantics = topicService.semanticSearch(q);
		return semantics;
	}
	
	@RequestMapping(value="/common/{topicId}")
	@ResponseBody
	public List<Topic> commonTags(@PathVariable Long topicId){
		
		Topic t  = topicService.getTopicById(topicId);
		if(t == null){
			return null;
		}
		List<Topic> commons = topicService.getTopicsWithCommonTag(t);
		
		return commons;
	}
	
	

	
	
	@RequestMapping(value="/common2/{topicId}")
	@ResponseBody
	public List<Topic> commonTags2(@PathVariable Long topicId){
		
		Topic t  = topicService.getTopicById(topicId);
		if(t == null){
			return null;
		}
		List<Topic> commons = topicService.getTopicsWithCommonTag(t);
		
		return commons;
	}
	
}
