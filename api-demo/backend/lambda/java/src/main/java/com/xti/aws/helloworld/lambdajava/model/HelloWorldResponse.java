package com.xti.aws.helloworld.lambdajava.model;

public class HelloWorldResponse {
    private String message;

    public HelloWorldResponse() {
    }

    public HelloWorldResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
