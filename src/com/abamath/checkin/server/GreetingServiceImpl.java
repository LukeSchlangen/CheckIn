package com.abamath.checkin.server;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abamath.checkin.client.GreetingService;
import com.abamath.checkin.shared.User;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	private static AmazonDynamoDB dynamoDB;
	private final static String MEMBER_TABLE_NAME = "Members";
	private final static String HISTORY_TABLE_NAME = "check-in-times";
	private final static String END_POINT = "dynamodb.us-west-2.amazonaws.com";
	
	public GreetingServiceImpl() throws IOException {
		setupDB();
	}
	
	@Override
	public void buttonClick(User user) throws IllegalArgumentException {
		try {
			Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("Name", new AttributeValue().withS(user.getName()));
			key.put("Color", new AttributeValue().withS(user.getColor()));
			Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
			update.put("Status", new AttributeValueUpdate().withValue(new AttributeValue().withS(user.getStatus())));
			UpdateItemRequest updateRequest = new UpdateItemRequest()
					.withTableName(MEMBER_TABLE_NAME)
					.withKey(key)
					.withAttributeUpdates(update);
			dynamoDB.updateItem(updateRequest);

			key = new HashMap<String, AttributeValue>();
			key.put("Time", new AttributeValue().withS(new Timestamp(new Date().getTime()).toString()));
			key.put("Member", new AttributeValue().withS(user.getName()));
			PutItemRequest putRequest = new PutItemRequest()
				.withTableName(HISTORY_TABLE_NAME)
				.withItem(key);
			dynamoDB.putItem(putRequest);
			System.out.println("here");
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<User> getUsers() {
		List<User> userList = new ArrayList<User>();
		ScanRequest request = new ScanRequest().withTableName(MEMBER_TABLE_NAME);
		ScanResult result = dynamoDB.scan(request);
			
		for(Map<String, AttributeValue> map : result.getItems()) {
			User user = new User();				
			user.setName(map.get("Name").getS());
			user.setColor(map.get("Color").getS());
			user.setStatus(map.get("Status").getS());
			userList.add(user);
		}
					
		return userList;		
	}
	
	private static void setupDB() throws IOException {
		
		AWSCredentials credentials = new PropertiesCredentials(GreetingServiceImpl.class.getResourceAsStream("AWSCredentials.properties"));
		dynamoDB = new AmazonDynamoDBClient(credentials);
		dynamoDB.setEndpoint(END_POINT);
	}
	
}
