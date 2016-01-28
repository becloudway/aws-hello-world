package com.xti.aws.helloworld.ebjava.api;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class HelloWorldApplication extends ResourceConfig {

    public HelloWorldApplication(){
	 	packages(true, "com.xti.aws.helloworld.ebjava.api");
		register(JacksonFeature.class);
        register(CORSResponseFilter.class);
    }

}
