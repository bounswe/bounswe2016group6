package org.learner.web.controller;

import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.learner.web.util.WikidataEntity;
import org.learner.web.util.WikidataSearch;
import org.learner.web.util.WikidataSearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.learner.persistence.dao.TagRepository;
import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
@Controller
@RequestMapping("/tag")
public class TagController {
	
	@Autowired
	public ITopicService topicService;
	
	@Autowired
	public TagRepository tagRepo;
	
	@RequestMapping(value="create")
	@ResponseBody
	public GenericResponse createTags(@RequestParam String[] tags,@RequestParam String[] context,@RequestParam final Long topicId){
		
		Topic topic = topicService.getTopicById(topicId);
		for(int i = 0;i<tags.length;i++){
			Tag tag = new Tag();
			tag.setName(tags[i]);
			tag.setContext(context[i]);
			tag.setCreatedAt(new Date());
			
			topicService.createTag(tag);
			
			if(topic !=null){
				topicService.createTagToTopic(topic, tag);
			}
			
		}
		
		return new GenericResponse("Tags created");
		
	}
	
	@PostMapping(value="/{topicId}/add", consumes="application/json")
	@ResponseBody
	public GenericResponse addTags(@PathVariable("topicId") Long topicId, @RequestBody List<Tag> tags){
		Topic topic = topicService.getTopicById(topicId);
		
		if(topic == null){
			return new GenericResponse("","Topic not found...");
		}
		
		System.out.println("Tags!");
		for (Tag tag : tags) {
			if(tag.getId() == null){
				System.out.println("NULL ID");
				Tag newtag = topicService.createTag(tag);
				topicService.createTagToTopic(topic, newtag);
			} else {
				System.out.println("NON_NULL ID");
				Tag existing = tagRepo.findOne(tag.getId());
				topicService.createTagToTopic(topic, existing);
			}
			System.out.println(tag.getId());
			System.out.println(tag.getName());
			System.out.println(tag.getContext());
		}
		return new GenericResponse("Successful!");
	}
	
	@RequestMapping(value="/suggest")
	@ResponseBody
	public List<Tag> tagSuggest(@RequestParam String query){
		System.out.println("Query: " + query);
		System.out.println("Tag suggest v1!");
		List<Tag> suggested = topicService.tagSuggest(query);
		
		//System.out.println("Suggested : " + suggested.get(0).getName());
		
		System.out.println("Deserialize starts");
		
		try {
			WikidataEntity wde = WikidataSearch.wikidataQuery(query);
			List<WikidataSearchModel> wdsm = wde.getSearch();
			
			
			for(WikidataSearchModel w: wdsm){
				Tag t = new Tag();
				t.setContext(w.getDescription());
				t.setName(w.getLabel());
				System.out.println("Wikidata TAgname : " + t.getName());
				System.out.println("Wikidata TAgcontext : " + t.getContext());
				suggested.add(t);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return suggested;
	}
	
	@RequestMapping(value="/{topicId}/remove/{tagId}")
	@ResponseBody
	public GenericResponse removeTagFromTopic(@PathVariable Long topicId,@PathVariable Long tagId,HttpServletResponse response){
		Topic topic = topicService.getTopicById(topicId);
		if(topic == null){
			return new GenericResponse("", "Topic not found!");
		} 
		Tag dbtag = tagRepo.findOne(tagId);
		boolean success = topicService.deleteTagFromTopic(topic, dbtag);
		
		if(!success){
			return new GenericResponse("","Does not exists");
		}
		return new GenericResponse("Tag removal successful!");
	}
	
	
	@RequestMapping(value="/{tagId}")
	@ResponseBody
	public List<Topic> topicsHavingTags(@PathVariable Long tagId){
		List<Topic> relatedTopics = topicService.getRelatedTopicsViaTags(tagId);
		for(Topic ttt : relatedTopics)
			System.out.println(" h : " + ttt.getHeader());
		return relatedTopics;
	}
}
