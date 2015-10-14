package com.xti.aws.helloworld.ebjava.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by Michael on 14/10/2015.
 */
public class JacksonFeature implements Feature {

    /** This method is what actually gets called,
     when your ResourceConfig registers a Feature. */
    @Override
    public boolean configure(FeatureContext context) {
        context.register(new JacksonJaxbJsonProvider());
        return true;
    }
}
