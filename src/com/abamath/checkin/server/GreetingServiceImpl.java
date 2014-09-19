package com.abamath.checkin.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abamath.checkin.client.GoogleWebProject;
import com.abamath.checkin.client.GreetingService;
import com.abamath.checkin.shared.User;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	private static AmazonDynamoDB dynamoDB;
	private final static String MEMBER_TABLE_NAME = "Members";
	private final static String END_POINT = "dynamodb.us-west-2.amazonaws.com";
	
	public GreetingServiceImpl() throws IOException {
		setupDB();
	}
	
	@Override
	public void greetServer(Map<String, String> user) throws IllegalArgumentException {
		try {
			// Store the items in a hashmap
			//Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
			// Put the data in the item
			//item.put("Id",
			//		new AttributeValue().withN("1"));
			//item.put("Name",
			//		new AttributeValue().withN("Tom"));
			//PutItemRequest itemRequest = new PutItemRequest()
			//		.withTableName("PUT_THE_TABLE_NAME_HERE")
			//		.withItem(item);
			// Make the request
			//dynamoDB.putItem(itemRequest);
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
