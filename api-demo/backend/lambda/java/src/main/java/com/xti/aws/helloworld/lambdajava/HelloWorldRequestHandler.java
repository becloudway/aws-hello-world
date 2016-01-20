package com.xti.aws.helloworld.lambdajava;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldRequest;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldResponse;

public class HelloWorldRequestHandler implements RequestHandler<HelloWorldRequest, HelloWorldResponse> {

    @Override
    public HelloWorldResponse handleRequest(HelloWorldRequest helloWorldRequest, Context context) {
        return new HelloWorldResponse("Hello " + helloWorldRequest.getIt());
    }
}