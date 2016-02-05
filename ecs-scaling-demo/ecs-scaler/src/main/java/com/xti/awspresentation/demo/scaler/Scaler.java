package com.xti.awspresentation.demo.scaler;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ecs.AmazonECSClient;
import com.amazonaws.services.ecs.model.Cluster;
import com.amazonaws.services.ecs.model.DescribeClustersResult;
import com.amazonaws.services.ecs.model.DescribeServicesRequest;
import com.amazonaws.services.ecs.model.DescribeServicesResult;
import com.amazonaws.services.ecs.model.ListClustersResult;
import com.amazonaws.services.ecs.model.ListServicesRequest;
import com.amazonaws.services.ecs.model.ListServicesResult;
import com.amazonaws.services.ecs.model.Service;
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
		
		DefaultAWSCredentialsProviderChain credentialsProvider = new DefaultAWSCredentialsProviderChain();
		
		AmazonECSClient ecsClient = new AmazonECSClient(credentialsProvider);
		ecsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
		
		String clusterName = "";
		String serviceName = "";
		
		//Get the cluster and service name
		//here we expect that our CloudFormation stack has name: "ECS-scaling-infrastructure"
		ListClustersResult lcr = ecsClient.listClusters();
		LOGGER.debug(lcr.getClusterArns());
		
		for (String clusterArn : lcr.getClusterArns()) {
			if(clusterArn.contains("ECS-scaling-infrastructure")) {
				clusterName = clusterArn;
				break;
			}
		}
		LOGGER.debug(clusterName);
		ListServicesResult lsr = ecsClient.listServices(new ListServicesRequest().withCluster(clusterName));
		LOGGER.debug(lsr.getServiceArns());
		DescribeServicesResult dsr = ecsClient.describeServices(new DescribeServicesRequest()
																		.withServices(lsr.getServiceArns())
																		.withCluster(clusterName));
		for (Service service : dsr.getServices()) {
			LOGGER.debug(service.getServiceName());
			if(service.getServiceName().startsWith("ECS-scaling-infrastructure")) {
				serviceName = service.getServiceName();
				break;
			}
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		SNSRecord snsRecord = snsRecords.get(0);
		String snsJsonMessage = snsRecord.getSNS().getMessage();
		
		SnsMessage snsMessage = objectMapper.readValue(snsJsonMessage, SnsMessage.class);
		
		int requiredTasks = 1;
		if(!"OK".equals(snsMessage.getNewState())) {
			requiredTasks = 8;
		}
		
		UpdateServiceRequest usr = new UpdateServiceRequest()
											.withCluster(clusterName)
											.withDesiredCount(requiredTasks)
											.withService(serviceName);
		ecsClient.updateService(usr);
		
		return "ok";
	}

}
