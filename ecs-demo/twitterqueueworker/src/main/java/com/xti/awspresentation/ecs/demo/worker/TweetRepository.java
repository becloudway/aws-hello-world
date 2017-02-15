package com.xti.awspresentation.ecs.demo.worker;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TweetRepository {

    private Table tweetsTable;

    public TweetRepository(@Value("${TWEET_TABLE}") String tweetTable) {
        DynamoDB dynamoDB = new DynamoDB(Regions.EU_WEST_1);
        tweetsTable = dynamoDB.getTable(tweetTable);
    }

    public Optional<String> getTweetByQuery(String query){
        Item tweet = tweetsTable.getItem(new GetItemSpec().withPrimaryKey("query", query));
        if(tweet != null){
            return Optional.of(tweet.getString("tweet"));
        } else {
            return Optional.empty();
        }
    }

    public void saveTweet(String query, String tweet){
        Item item = new Item()
                .withString("query", query)
                .withString("tweet", tweet);
        tweetsTable.putItem(item);
    }
}
