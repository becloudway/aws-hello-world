package com.xti.awspresentation.ecs.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Optional;

@Controller
@EnableAutoConfiguration
public class TwitterAPIController {

    @Autowired
    private TwitterQueueComponent twitterQueueComponent;

    @Autowired
    private TweetRepository tweetRepository;

    @RequestMapping("/")
    @ResponseBody
    String start() {
        return "Add the keyword after the slash";
    }

    @RequestMapping("/{keyword}")
    @ResponseBody
    String query(@PathVariable("keyword") String keyword, @RequestParam(value = "reset", required = false) boolean reset) throws IOException {
        Optional<String> tweetByQuery = tweetRepository.getTweetByQuery(keyword);
        if(tweetByQuery.isPresent() && !reset){
            return tweetByQuery.get();
        } else {
            twitterQueueComponent.addToQueue(new TwitterSearchRequest(keyword));
            return "added to queue";
        }
    }
}