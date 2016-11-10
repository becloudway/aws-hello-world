package com.xti.awspresentation.demo.api.user;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class UserApiConfig extends ResourceConfig {

    public UserApiConfig() {
        register(JacksonFeature.class);
        packages(true, "com.xti.awspresentation.demo.api.user");
    }
}
