package com.xti.awspresentation.ecs.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TwitterQueueComponent {

    @Autowired
    protected JmsTemplate defaultJmsTemplate;

    @Value("${TWEET_QUEUE}")
    private String tweetQueue;

    public void addToQueue(TwitterSearchRequest request) throws IOException {
        defaultJmsTemplate.convertAndSend(tweetQueue, request.toJSON());
    }

}