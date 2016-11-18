package org.learner.web.controller;

import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import org.learner.persistence.model.Tag;
import org.learner.persistence.model.Topic;
@Controller
@RequestMapping("/tag")
public class TagController {
	@Autowired
	public ITopicService topicService;
	
	
	
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
	
	
	@RequestMapping(value="/suggest")
	@ResponseBody
	public List<Tag> tagSuggest(@RequestParam String query){
		System.out.println("Query: " + query);
		System.out.println("Tag suggest v1!");
		List<Tag> suggested = topicService.tagSuggest(query);
		
		System.out.println("Suggested : " + suggested.get(0).getName());
		
		return suggested;
	}
	
}
