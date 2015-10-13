package com.xti.aws.helloworld.ebjava.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloApi {

	@GET
	@Path("{it}")
	public Response getHello(@PathParam("it") String it){
		return Response.ok(new HelloResponse("Hello" + it), MediaType.APPLICATION_JSON_TYPE).build();
	}
}
