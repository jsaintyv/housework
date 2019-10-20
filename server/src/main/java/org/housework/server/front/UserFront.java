package org.housework.server.front;

import org.housework.server.models.User;

public class UserFront {
	private final User user;
	
	public UserFront(User user) {
		this.user = user;
	}
	
	public String getLogin() {
		return user.getLogin();
	}

	public String getRole() {
		return user.getRole();
	}	
}
