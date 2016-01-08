package com.xti.aws.helloworld.lambdajava;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldRequest;
import com.xti.aws.helloworld.lambdajava.model.HelloWorldResponse;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HelloWorldRequestHandlerTest {

    @Test
    public void testHelloWorldRequest() {
        HelloWorldRequest helloWorldRequest = new HelloWorldRequest();
        helloWorldRequest.setIt("XT-i");
        Context context = new ContextImplementation();

        HelloWorldRequestHandler helloWorldRequestHandler = new HelloWorldRequestHandler();
        HelloWorldResponse helloWorldResponse = helloWorldRequestHandler.handleRequest(helloWorldRequest, context);

        assertEquals("Hello XT-i", helloWorldResponse.getMessage());
    }

    private class ContextImplementation implements Context {

        @Override
        public String getAwsRequestId() {
            return null;
        }

        @Override
        public String getLogGroupName() {
            return null;
        }

        @Override
        public String getLogStreamName() {
            return null;
        }

        @Override
        public String getFunctionName() {
            return null;
        }

        @Override
        public String getFunctionVersion() {
            return null;
        }

        @Override
        public String getInvokedFunctionArn() {
            return null;
        }

        @Override
        public CognitoIdentity getIdentity() {
            return null;
        }

        @Override
        public ClientContext getClientContext() {
            return null;
        }

        @Override
        public int getRemainingTimeInMillis() {
            return 0;
        }

        @Override
        public int getMemoryLimitInMB() {
            return 0;
        }

        @Override
        public LambdaLogger getLogger() {
            return null;
        }
    }
}
