package com.abamath.checkin.client;

import java.util.List;

import com.abamath.checkin.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("abamathServlet")
public interface AbamathService extends RemoteService {
	
	void buttonClick(User user) throws IllegalArgumentException;
	
	List<User> getUsers();
	
	boolean createAccount(String username, String password);
	
	boolean authenticate(String username, String password);
}
