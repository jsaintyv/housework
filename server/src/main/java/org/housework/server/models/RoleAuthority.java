package org.housework.server.models;

import org.springframework.security.core.GrantedAuthority;

public enum RoleAuthority implements GrantedAuthority {
	ADMIN, USER;

	@Override
	public String getAuthority() {
		System.out.println(name());
		return name();
	}
	
}
