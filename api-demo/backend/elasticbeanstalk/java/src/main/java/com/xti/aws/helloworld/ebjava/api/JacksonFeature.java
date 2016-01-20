package com.xti.aws.helloworld.ebjava.api;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by Michael on 14/10/2015.
 */
public class JacksonFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new JacksonJaxbJsonProvider());
        return true;
    }
}
