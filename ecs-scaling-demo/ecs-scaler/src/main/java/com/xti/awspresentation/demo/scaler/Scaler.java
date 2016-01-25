package com.xti.awspresentation.demo.scaler;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.ecs.AmazonECSClient;
import com.amazonaws.services.ecs.model.UpdateServiceRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Scaler {
	
	private static final Logger LOGGER = LogManager.getLogger(Scaler.class);
	
	public String handler(SNSEvent input, Context context) throws JsonParseException, JsonMappingException, IOException {
		
		List<SNSRecord> snsRecords = input.getRecords();
		
		for (SNSRecord snsRecord : snsRecords) {
			LOGGER.debug(snsRecord.getEventSource());
			LOGGER.debug(snsRecord.getEventSubscriptionArn());
			LOGGER.debug(snsRecord.getEventVersion());
			LOGGER.debug(snsRecord.getSNS().getMessage());
			LOGGER.debug(snsRecord.getSNS().getSubject());
			LOGGER.debug(snsRecord.getSNS().getTopicArn());
			LOGGER.debug(snsRecord.getSNS().getType());
		}
		
		SnsMessage snsMessage = new ObjectMapper().readValue(snsRecords.get(0).getSNS().getMessage(), SnsMessage.class);
		
		String clusterName = "SQS-with-cloudwatch-alarm-mycluster-UVYW8ZMWDW";
		String serviceName = "SQS-with-cloudwatch-alarm-ECSService-1VFF1AQ5OC23F";
		
		DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
		
		AmazonECSClient ecsClient = new AmazonECSClient(credentialsProvider);
		
		int requiredTasks = 1;
		if(!"OK".equals(snsMessage.getNewState())) {
			requiredTasks = 8;
		}
		
		UpdateServiceRequest usr = new UpdateServiceRequest();
		usr.withCluster(clusterName)
			.withDesiredCount(requiredTasks)
			.withService(serviceName);
		ecsClient.updateService(usr);
		
		return "ok";
	}

}
