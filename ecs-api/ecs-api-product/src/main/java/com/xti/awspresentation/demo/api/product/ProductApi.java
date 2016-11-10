package com.xti.awspresentation.demo.api.product;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/product")
@Consumes({"application/json"})
@Produces({"application/json"})
public class ProductApi {

    private static final String IDENTIFIER = UUID.randomUUID().toString();

    @GET
    @Path("{product-id}")
    public GetProductResponse getUser(@PathParam("product-id") String productId) {
        return new GetProductResponse(IDENTIFIER, productId);
    }

    @GET
    @Path("/health-check")
    public Response healthCheck() {
        return Response.ok().build();
    }
}
