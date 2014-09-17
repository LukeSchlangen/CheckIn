package com.abamath.checkin.client;

import java.util.List;
import java.util.Map;

import com.abamath.checkin.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(Map<String, String> user, AsyncCallback<Void> asyncCallback);
	void getUsers(AsyncCallback<List<User>> asyncCallback);
}
