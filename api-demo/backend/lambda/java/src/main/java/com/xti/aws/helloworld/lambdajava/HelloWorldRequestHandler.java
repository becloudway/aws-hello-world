package com.xti.aws.helloworld.lambdajava;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldRequest;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldResponse;

/**
 * Lambda handler: com.xti.aws.helloworld.lambdajava.HelloWorldRequestHandler
 * Api Gateway mapping
 #set($inputRoot = $input.path('$'))
 {
 "it" : "$input.params('it')"
 }
 */
public class HelloWorldRequestHandler implements RequestHandler<HelloWorldRequest, HelloWorldResponse> {

    public HelloWorldRequestHandler() {
    }

    @Override
    public HelloWorldResponse handleRequest(HelloWorldRequest helloWorldRequest, Context context) {
        return new HelloWorldResponse("Hello " + helloWorldRequest.getIt() + " from java8 lambda.");
    }
}
