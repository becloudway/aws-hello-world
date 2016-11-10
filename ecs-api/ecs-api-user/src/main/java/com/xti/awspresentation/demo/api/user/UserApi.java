package com.xti.awspresentation.demo.api.user;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user")
@Consumes({"application/json"})
@Produces({"application/json"})
public class UserApi {

    private static final String IDENTIFIER = UUID.randomUUID().toString();

    @GET
    @Path("/{user-id}")
    public Response getUser(@PathParam("user-id") String userId) {
        return Response.ok().entity(new GetUserResponse(IDENTIFIER, userId)).build();
    }

    @GET
    @Path("/health-check")
    public Response healthCheck() {
        return Response.ok().build();
    }

}
