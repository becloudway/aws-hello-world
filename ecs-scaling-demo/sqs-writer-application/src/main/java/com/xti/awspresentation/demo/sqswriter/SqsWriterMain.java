package com.xti.awspresentation.demo.sqswriter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

public class SqsWriterMain {
	
	public static void main(String[] args) throws InterruptedException {
		AmazonSQSClient SQSClient = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain());
		
		String sqsUrl = "https://sqs.us-east-1.amazonaws.com/335317431711/ECS-scaling-infrastructure-MyQueue-1LP2UQK4SH6EH";
		
		List<SendMessageBatchRequestEntry> messages = new ArrayList<>();
		
		while(true) {
			int index = 0;
			
			while(index < 10) {
				messages.add(new SendMessageBatchRequestEntry(UUID.randomUUID().toString(), "Message " + index + " send on " + System.currentTimeMillis()));
				index++;
			}
			
			SQSClient.sendMessageBatch(sqsUrl, messages);
			messages.clear();
			
			Thread.sleep(250);
		}
	}

}
