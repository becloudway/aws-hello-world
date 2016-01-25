package com.xti.awspresentation.demo.sqsreader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.logs.AWSLogsClient;
import com.amazonaws.services.logs.model.CreateLogStreamRequest;
import com.amazonaws.services.logs.model.InputLogEvent;
import com.amazonaws.services.logs.model.PutLogEventsRequest;
import com.amazonaws.services.logs.model.PutLogEventsResult;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class SqsReaderMain {
	
	public static void main(String[] args) throws InterruptedException {
		DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
		
		AmazonSQSClient SQSClient = new AmazonSQSClient(credentialsProvider);
		AWSLogsClient logClient = new AWSLogsClient(credentialsProvider);
		
		String sqsUrl = "https://sqs.us-east-1.amazonaws.com/335317431711/SQS-with-cloudwatch-alarm-MyQueue-1IWL7SIRMQ7HY";
		String logGroupName = "SQS-reader";
		String logStreamName = "logs" + DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS'Z'").format(LocalDateTime.now()) + UUID.randomUUID().toString();
		
		String sequenceToken = null;
		
		logClient.createLogStream(new CreateLogStreamRequest().withLogGroupName(logGroupName).withLogStreamName(logStreamName));
		
		while(true) {
			ReceiveMessageResult result = SQSClient.receiveMessage(new ReceiveMessageRequest(sqsUrl).withMaxNumberOfMessages(10));

			List<InputLogEvent> logEvents = new ArrayList<>();
			
			if(result.getMessages().size() > 0) {
				for (Message message : result.getMessages()) {
					logEvents.add(new InputLogEvent()
										.withMessage("SQS message: " + message.getBody())
										.withTimestamp(System.currentTimeMillis()));
					SQSClient.deleteMessage(new DeleteMessageRequest(sqsUrl, message.getReceiptHandle()));
				}
				
				
				PutLogEventsResult logResult = logClient.putLogEvents(new PutLogEventsRequest(logGroupName, logStreamName, logEvents).withSequenceToken(sequenceToken));
				
				sequenceToken = logResult.getNextSequenceToken();
			} else {
				Thread.sleep(200);
			}
			
		}
	}

}
