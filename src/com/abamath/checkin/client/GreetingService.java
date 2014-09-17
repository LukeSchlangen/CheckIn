package com.abamath.checkin.client;

import java.util.List;
import java.util.Map;

import com.abamath.checkin.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	void greetServer(Map<String,String> user) throws IllegalArgumentException;
	List<User> getUsers();
}
