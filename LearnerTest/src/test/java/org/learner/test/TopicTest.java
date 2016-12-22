package org.learner.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import org.learner.service.ITopicService;
import org.learner.service.TopicService;
import org.learner.spring.ServiceConfig;
import org.learner.spring.TestDbConfig;
import org.learner.web.dto.TopicDto;
import org.learner.web.dto.UserDto;
import org.learner.persistence.dao.TopicRepository;
import org.learner.persistence.model.Topic;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TestDbConfig.class, ServiceConfig.class })
public class TopicTest {
	
	@Autowired
	private ITopicService topicService;

	@Autowired
	private TopicRepository topicRepo;
	
	final String header = "Header Test";
	final String content = "Content Test";
	
	@Test
	public void createNewTopic_thenCheckFromRepository(){
		final TopicDto topicDto = createTopicDto();
		Topic mytop = topicService.createNewTopic(topicDto);
		
		assertNotNull(mytop);
		assertNotNull(mytop.getHeader());
		assertNotNull(mytop.getContent());
		
		assertEquals(mytop.getHeader(),header);
		assertEquals(mytop.getContent(),content);
		
		topicRepo.delete(mytop);
		
	}
	
    private TopicDto createTopicDto() {
        final TopicDto topicDto = new TopicDto();
        topicDto.setHeader(header);
        topicDto.setContent(content);
        return topicDto;
    }

}
