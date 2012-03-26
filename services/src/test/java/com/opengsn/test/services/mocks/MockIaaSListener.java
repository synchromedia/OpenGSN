package com.opengsn.test.services.mocks;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class MockIaaSListener implements MessageListener {
	public void onMessage(Message message) {

	}
}
