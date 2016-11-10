package com.xti.awspresentation.demo.api.product;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ProductApiConfig extends ResourceConfig {

    public ProductApiConfig() {
        register(JacksonFeature.class);
        packages(true, "com.xti.awspresentation.demo.api.product");
    }
}
