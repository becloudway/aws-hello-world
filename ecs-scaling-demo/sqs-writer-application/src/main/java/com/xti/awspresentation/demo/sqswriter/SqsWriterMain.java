package com.xti.awspresentation.demo.sqswriter;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SqsWriterMain {
	
	public static void main(String[] args) throws InterruptedException {
		AmazonSQSClient SQSClient = new AmazonSQSClient(new DefaultAWSCredentialsProviderChain());
		
		String sqsUrl = System.getenv("SQS_URL");
		
		List<SendMessageBatchRequestEntry> messages = new ArrayList<>();

        int i = 0;
		
		while(i < 500) {
			int index = 0;

			while(index < 10) {
				messages.add(new SendMessageBatchRequestEntry(UUID.randomUUID().toString(), "Message " + i + " send on " + System.currentTimeMillis()));
				index++;
			}

			SQSClient.sendMessageBatch(sqsUrl, messages);
			messages.clear();
			
			//Thread.sleep(250);
            i++;
		}
	}

}
