package org.learner.registration.listener;

import org.learner.registration.TopicEvent;
import org.springframework.context.ApplicationListener;

public class TopicListener implements ApplicationListener<TopicEvent>{

	@Override
	public void onApplicationEvent(TopicEvent event) {
		// TODO Topic creation extra events
		
	}

}
