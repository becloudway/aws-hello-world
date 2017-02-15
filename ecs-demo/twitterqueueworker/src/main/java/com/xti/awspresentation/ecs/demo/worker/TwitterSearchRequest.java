package com.xti.awspresentation.ecs.demo.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TwitterSearchRequest {

    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static TwitterSearchRequest fromJSON(String json)
                               throws JsonProcessingException, IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(json, TwitterSearchRequest.class);
    }
}