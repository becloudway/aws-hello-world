package com.xti.aws.helloworld.ebjava.api;

/**
 * Created by IntelliJ IDEA.
 * User: declewi
 * Date: 13/10/15
 * Time: 17:03
 */
public class HelloResponse {
	private String message;

	public HelloResponse() {
	}

	public HelloResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
