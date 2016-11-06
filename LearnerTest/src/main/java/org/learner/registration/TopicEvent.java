package org.learner.registration;

import org.learner.persistence.model.Topic;
import org.springframework.context.ApplicationEvent;

public class TopicEvent extends ApplicationEvent {

	public TopicEvent(final Topic topic) {
		super(topic);
		// TODO Auto-generated constructor stub
	}

}
