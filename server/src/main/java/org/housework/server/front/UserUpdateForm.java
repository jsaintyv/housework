package org.housework.server.front;

import org.housework.server.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserUpdateForm {
	private String name;
		
	private Boolean updatePassword;
		
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getUpdatePassword() {
		return updatePassword;
	}

	public void setUpdatePassword(Boolean updatePassword) {
		this.updatePassword = updatePassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void copyInto(User user) {
		user.setName(this.getName());
		
		if(Boolean.TRUE.equals(this.updatePassword)) {
			user.setPassword(new BCryptPasswordEncoder().encode(this.getPassword()));
		}
	}
	
	
	
}
