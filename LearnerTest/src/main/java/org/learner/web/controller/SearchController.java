package org.learner.web.controller;

import java.util.List;

import org.learner.persistence.model.Comment;
import org.learner.persistence.model.Topic;
import org.learner.service.ITopicService;
import org.learner.web.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/search")
public class SearchController {
	@Autowired
	ITopicService topicService;
	
	
	
	@RequestMapping(value = "/")
	@ResponseBody
	public List<Topic> basicSearch(@RequestParam String keyword){
		System.out.println("HELLOWORLD");
		if(keyword == null){
			return null;
		}
		
		return topicService.basicKeywordSearch(keyword);
		
	}
}
