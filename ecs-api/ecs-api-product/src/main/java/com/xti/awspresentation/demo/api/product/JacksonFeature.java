package com.xti.awspresentation.demo.api.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class JacksonFeature implements Feature {
 
    private static final ObjectMapper mapper =
        new ObjectMapper(){{
        }};
 
    private static final JacksonJaxbJsonProvider provider =
        new CustomJacksonProvider(){{
            setMapper(mapper);
        }};

    /** This method is what actually gets called,
        when your ResourceConfig registers a Feature. */
    public boolean configure(FeatureContext context) {
        context.register(provider);
        return true;
    }
}