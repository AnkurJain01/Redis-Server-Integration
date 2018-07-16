package com.example.redis.impl;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class RedisMessageListener implements MessageListener {

	public void onMessage(Message message,  byte[] pattern) {
		System.out.println( "Message received: " + message.toString() );
		
	}
}