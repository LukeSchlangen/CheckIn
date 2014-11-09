package com.abamath.checkin.server;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.abamath.checkin.client.AbamathService;
import com.abamath.checkin.shared.User;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AbamathServiceImpl extends RemoteServiceServlet implements
		AbamathService {
	private static AmazonDynamoDB dynamoDB;
	
	private String memberTableName;
	private String historyTableName;
	
	private final static String AUTHENTICATION_TABLE_NAME = "Authentication";
	private final static String END_POINT = "dynamodb.us-west-2.amazonaws.com";
	private final static String MEMBER_TABLE_PRIMARY_KEY = "Name";
	private final static String MEMBER_TABLE_RANGE_KEY = "Color";
	private final static String CHECKIN_TABLE_PRIMARY_KEY = "Time";
	private final static String CHECKIN_TABLE_RANGE_KEY = "Member";
	private final static String TABLE_TYPE_STRING = "S";
	private final static long READ_TP = 1L;
	private final static long WRITE_TP = 1L;
	

	public AbamathServiceImpl() throws IOException {
		setupDB();
	}

	@Override
	public void buttonClick(User user) throws IllegalArgumentException {
		try {
			Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
			key.put("Name", new AttributeValue().withS(user.getName()));
			Map<String, AttributeValueUpdate> update = new HashMap<String, AttributeValueUpdate>();
			key.put("Color", new AttributeValue().withS(user.getColor()));
			update.put("Status", new AttributeValueUpdate().withValue(new AttributeValue().withS(user.getStatus())));
			if (user.getStatus().equals("Out")) {
				update.put("Time", new AttributeValueUpdate().withValue(new AttributeValue().withS(user.getTime())));
			}
			UpdateItemRequest updateRequest = new UpdateItemRequest()
					.withTableName(memberTableName).withKey(key)
					.withAttributeUpdates(update);
			dynamoDB.updateItem(updateRequest);

			key = new HashMap<String, AttributeValue>();
			key.put("Time", new AttributeValue().withS(new Timestamp(new Date()
					.getTime()).toString()));
			key.put("Member", new AttributeValue().withS(user.getName()));
			PutItemRequest putRequest = new PutItemRequest().withTableName(historyTableName)
					.withItem(key);
			dynamoDB.putItem(putRequest);

		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<User> getUsers(String adminUser) {
		
		if(adminUser == null) {
			//Something has gone wrong, no user log-in detected
			//Log an error message
			throw new IllegalArgumentException();
		}
		else {
			memberTableName = adminUser + "_members_1";
			historyTableName = adminUser + "_checkin_1";
		}
		
		List<User> userList = new ArrayList<User>();
		ScanRequest request = new ScanRequest()
				.withTableName(memberTableName);
		ScanResult result = dynamoDB.scan(request);

		for (Map<String, AttributeValue> map : result.getItems()) {
			User user = new User();
			user.setName(map.get("Name").getS());
			user.setColor(map.get("Color").getS());
			user.setStatus(map.get("Status").getS());
			user.setTime(map.get("Time").getS());
			userList.add(user);
		}

		return userList;
	}

	@Override
	public boolean createAccount(String username, String password) {
		if (username.equals("") || password.equals("")) {
			return false;
		}

		//create member table for the user
		ArrayList<KeySchemaElement> ksMember = new ArrayList<KeySchemaElement>();
		ArrayList<AttributeDefinition> adMember = new ArrayList<AttributeDefinition>();

		//Members - primary key
		ksMember.add(new KeySchemaElement()
			.withAttributeName(MEMBER_TABLE_PRIMARY_KEY)
			.withKeyType(KeyType.HASH));
		adMember.add(new AttributeDefinition()
			.withAttributeName(MEMBER_TABLE_PRIMARY_KEY)
			.withAttributeType(TABLE_TYPE_STRING));
		
		//Color - range key
		ksMember.add(new KeySchemaElement()
			.withAttributeName(MEMBER_TABLE_RANGE_KEY)
			.withKeyType(KeyType.RANGE));
		adMember.add(new AttributeDefinition()
			.withAttributeName(MEMBER_TABLE_RANGE_KEY)
			.withAttributeType(TABLE_TYPE_STRING));
		
		ProvisionedThroughput ptMember = new ProvisionedThroughput()
			.withReadCapacityUnits(READ_TP)
			.withWriteCapacityUnits(WRITE_TP);
		
		String membersTableName = username + "_members_" + 1;
		
		CreateTableRequest membersTableRequest = new CreateTableRequest()
			.withTableName(membersTableName)
			.withKeySchema(ksMember)
			.withProvisionedThroughput(ptMember)
			.withAttributeDefinitions(adMember);
		
		dynamoDB.createTable(membersTableRequest);
		
		//create checkin times table for the user
		ArrayList<KeySchemaElement> ksCheckin = new ArrayList<KeySchemaElement>();
		ArrayList<AttributeDefinition> adCheckin = new ArrayList<AttributeDefinition>();

		//Time - primary key
		ksCheckin.add(new KeySchemaElement()
			.withAttributeName(CHECKIN_TABLE_PRIMARY_KEY)
			.withKeyType(KeyType.HASH));
		adCheckin.add(new AttributeDefinition()
			.withAttributeName(CHECKIN_TABLE_PRIMARY_KEY)
			.withAttributeType(TABLE_TYPE_STRING));
		
		//Member - range key
		ksCheckin.add(new KeySchemaElement()
			.withAttributeName(CHECKIN_TABLE_RANGE_KEY)
			.withKeyType(KeyType.RANGE));
		adCheckin.add(new AttributeDefinition()
			.withAttributeName(CHECKIN_TABLE_RANGE_KEY)
			.withAttributeType(TABLE_TYPE_STRING));
		
		ProvisionedThroughput pt = new ProvisionedThroughput()
			.withReadCapacityUnits(READ_TP)
			.withWriteCapacityUnits(WRITE_TP);
		
		String checkinTableName = username + "_checkin_" + 1;
		
		CreateTableRequest checkinTableRequest = new CreateTableRequest()
			.withTableName(checkinTableName)
			.withKeySchema(ksCheckin)
			.withProvisionedThroughput(pt)
			.withAttributeDefinitions(adCheckin);
		
		dynamoDB.createTable(checkinTableRequest);

		// create the account in the auth table
		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Username", new AttributeValue().withS(username));
		key.put("Password", new AttributeValue().withS(password));
		key.put("Salt", new AttributeValue().withS(password));
		key.put("Tables", new AttributeValue().withN("1"));
		PutItemRequest putRequest = new PutItemRequest()
			.withTableName(AUTHENTICATION_TABLE_NAME)
			.withItem(key);
		dynamoDB.putItem(putRequest);

		return true;
	}

	@Override
	public boolean authenticate(String username, String password) {
		if (password.isEmpty()) {
			return false;
		}
		Map<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Username", new AttributeValue().withS(username));
		key.put("Password", new AttributeValue().withS(password));
		GetItemRequest getRequest = new GetItemRequest().withTableName(
				AUTHENTICATION_TABLE_NAME).withKey(key);
		GetItemResult getResult = dynamoDB.getItem(getRequest);
		if (getResult.getItem() == null) {
			// username doesn't exist in the db
			return false;
		} else {
			if (getResult.getItem().get("Password").getS().equals(password)) {
				// if user exists and passwords match
				return true;
			}
		}

		return false;
	}
	
	@Override
	public void addUser(String username, String color) {
		HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
		key.put("Name", new AttributeValue().withS(username));
		key.put("Color", new AttributeValue().withS(color));
		key.put("Status", new AttributeValue().withS("Out"));
		key.put("Time", new AttributeValue().withS("0"));
		PutItemRequest putRequest = new PutItemRequest()
			.withTableName(memberTableName)
			.withItem(key);
		dynamoDB.putItem(putRequest);		
	}

	private static void setupDB() throws IOException {

		AWSCredentials credentials = new PropertiesCredentials(
				AbamathServiceImpl.class
						.getResourceAsStream("AWSCredentials.properties"));
		dynamoDB = new AmazonDynamoDBClient(credentials);
		dynamoDB.setEndpoint(END_POINT);
	}
}
