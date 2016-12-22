package org.learner.web.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.web.bind.annotation.RequestMethod;
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
	public String basicSearchResults(@RequestParam String q,
									Model model){
		System.out.println("HELLOWORLD");
		if(q == null){
			return null;
		} 
		
		if(q.length() <2){
			return "redirect:/home";
		}
		
		
		List<Topic> queryResults = topicService.semanticSearch(q);
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
	
	@RequestMapping(value = "/search/pack/{packId}")
	public String packSearchResults(@PathVariable Long packId,Model model){
		List<Topic> relatedTopics = topicService.getTopicsInPack(packId);
		
		List<Topic> queryResults = relatedTopics == null ? new ArrayList<Topic>(): relatedTopics;
		model.addAttribute("searchTitle", "Topics in pack : ");
		model.addAttribute("topics", queryResults);
		return "searchresult";
	}
	
	@RequestMapping(value = "/advancedSearch" , method=RequestMethod.POST)
	public String searchWithCriteriaPage(@RequestParam("topicPackId") String topicPackId,
										  @RequestParam("afterDate") String afterDate, 
										  @RequestParam("teacherId") String teacherId,
										  @RequestParam("q") String q, final Model model){
		System.out.println("WOLOLO");
		
		
		final long packId ;
		final Date dt ;
		final long teaId ;
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
		
		List<Topic> allTopics = new ArrayList<>();
		if(q != null){
			if(q.length() >2){
				allTopics = topicService.semanticSearch(q);
			} else {
				allTopics = topicService.getAllTopics();
			}
		} else {
			allTopics = topicService.getAllTopics();
		}
		
		try {
			packId = Long.parseLong(topicPackId);
			allTopics = allTopics.stream()
					.filter(p -> p.getTopicPack().getId() == packId).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Pack not found");
		}
		
		try {
			teaId = Long.parseLong(teacherId);
			allTopics = allTopics.stream()
					.filter(p -> p.getOwner().getId() == teaId).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Teacher not found");
		}
		
		try {
			dt = df.parse(afterDate);
			allTopics = allTopics.stream()
					.filter(p -> p.getCreatedAt().after(dt)).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Date not found!");
		}
		
		model.addAttribute("topics", allTopics);
		return "searchresult";
	}
	
	
	
	
	// REST API functions
	@RequestMapping(value = "/advancedSearch" , method=RequestMethod.GET)
	@ResponseBody
	public List<Topic> searchWithCriteria(@RequestParam("topicPackId") String topicPackId,
										  @RequestParam("afterDate") String afterDate, 
										  @RequestParam("teacherId") String teacherId,
										  @RequestParam("q") String q){
		System.out.println("WOLOLO");
		
		
		final long packId ;
		final Date dt ;
		final long teaId ;
		DateFormat df = new SimpleDateFormat("YYYY-MM-DD");
		
		List<Topic> allTopics = new ArrayList<>();
		if(q != null){
			if(q.length() >2){
				allTopics = topicService.semanticSearch(q);
			} else {
				allTopics = topicService.getAllTopics();
			}
		} else {
			allTopics = topicService.getAllTopics();
		}
		
		try {
			packId = Long.parseLong(topicPackId);
			allTopics = allTopics.stream()
					.filter(p -> p.getTopicPack().getId() == packId).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Pack not found");
		}
		
		try {
			teaId = Long.parseLong(teacherId);
			allTopics = allTopics.stream()
					.filter(p -> p.getOwner().getId() == teaId).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Teacher not found");
		}
		
		try {
			dt = df.parse(afterDate);
			allTopics = allTopics.stream()
					.filter(p -> p.getCreatedAt().after(dt)).collect(Collectors.toList());
		} catch (Exception e) {
			System.out.println("Date not found!");
		}
		
		return allTopics;
	}
	
	
	
	@RequestMapping(value = "/pack/search/{packId}")
	@ResponseBody
	public List<Topic> packTopics(@PathVariable Long packId,Model model){
		List<Topic> relatedTopics = topicService.getTopicsInPack(packId);
		
		List<Topic> queryResults = relatedTopics == null ? new ArrayList<Topic>(): relatedTopics;
		
		return queryResults;
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
