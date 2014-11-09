package com.abamath.checkin.client;

import java.util.List;

import com.abamath.checkin.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface AbamathServiceAsync {
	void buttonClick(User user, AsyncCallback<Void> asyncCallback);
	void getUsers(String adminUser, AsyncCallback<List<User>> asyncCallback);
	void createAccount(String username, String password,
			AsyncCallback<Boolean> callback);
	void authenticate(String username, String password, AsyncCallback<Boolean> callback);
	void addUser(String username, String color, AsyncCallback<Void> callback);
}
