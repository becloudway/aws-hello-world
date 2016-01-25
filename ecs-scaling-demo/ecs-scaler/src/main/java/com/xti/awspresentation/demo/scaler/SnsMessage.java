package com.xti.awspresentation.demo.scaler;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SnsMessage {
	
	private String newState;
	
	@JsonProperty("NewStateValue")
	public String getNewState() {
		return newState;
	}
	
	public void setNewState(String newState) {
		this.newState = newState;
	}

}
