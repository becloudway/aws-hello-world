package com.xti.awspresentation.demo.sqsreader;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class SqsReaderMain {
	
	public static void main(String[] args) throws InterruptedException {
		DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
		
		AmazonSQSClient SQSClient = new AmazonSQSClient(credentialsProvider);
		
		String sqsUrl = System.getenv("SQS_URL");
		
		while(true) {
			ReceiveMessageRequest rmr = new ReceiveMessageRequest(sqsUrl).withMaxNumberOfMessages(10);
			ReceiveMessageResult result = SQSClient.receiveMessage(rmr);
			
			if(result.getMessages().size() > 0) {
				for (Message message : result.getMessages()) {
					//process message
					SQSClient.deleteMessage(new DeleteMessageRequest(sqsUrl, message.getReceiptHandle()));
				}
			} else {
				Thread.sleep(200);
			}
			
		}
		
	}

}
