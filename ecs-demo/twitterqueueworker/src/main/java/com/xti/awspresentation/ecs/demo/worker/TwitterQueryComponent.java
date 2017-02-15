package com.xti.awspresentation.ecs.demo.worker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Component
public class TwitterQueryComponent {

    private Twitter twitter;

    public TwitterQueryComponent(@Value("${TWITTER_CONSUMER_KEY}") String consumerKey,
                                 @Value("${TWITTER_CONSUMER_SECRET}") String consumerSecret,
                                 @Value("${TWITTER_ACCESS_TOKEN}") String accessToken,
                                 @Value("${TWITTER_ACCESS_TOKEN_SECRET}") String accessTokenSecret) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public String getFirstTweetForQuery(String twitterQuery){
        Query query = new Query(twitterQuery);
        try {
            QueryResult result = twitter.search(query);
            if(result.getCount() > 0){
                return result.getTweets().get(0).getText();
            } else {
                return "Too bad, no tweet found.";
            }
        } catch (TwitterException e) {
            e.printStackTrace();
            return "Too bad, Twitter failed.";
        }
    }
}
