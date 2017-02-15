package com.xti.awspresentation.ecs.demo.worker;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.io.IOException;

@Service
public class TwitterQueryService {

    private Logger log = Logger.getLogger(TwitterQueryService.class);

    @Autowired
    private TwitterQueryComponent twitterQueryComponent;

    @Autowired
    private TweetRepository tweetRepository;

    @JmsListener(destination = "${TWEET_QUEUE}")
    public void storeTweet(String requestJSON) throws JMSException {
        log.info("Received: " + requestJSON);
        try {
            TwitterSearchRequest request = TwitterSearchRequest.fromJSON(requestJSON);
            String tweet = twitterQueryComponent.getFirstTweetForQuery(request.getQuery());
            tweetRepository.saveTweet(request.getQuery(), tweet);
            log.info("tweet: " + tweet);
        } catch (IOException ex) {
            throw new JMSException("Encountered error while parsing message.");
        }
    }

}