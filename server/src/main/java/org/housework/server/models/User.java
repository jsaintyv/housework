package org.housework.server.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
public class User implements UserDetails {
	private static final long serialVersionUID = 1L;

	// PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "login", unique=true)
    private String login;
    
    private String password;
    
    private boolean enabled;
    
    private long registeringCode;
    
    private String role;
           
	public User() {}
    	
    public int getId() {
		return id;
	}
    
	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonIgnore
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setRole(RoleAuthority role) {
		this.role = role.name();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		RoleAuthority role = RoleAuthority.USER;
		if(this.role != null) {
			role = RoleAuthority.valueOf(this.role);
		}
		return Arrays.asList(role);
	}

	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() { 
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
	
	@JsonIgnore
	public long getRegisteringCode() {
		return registeringCode;
	}

	@JsonIgnore
	public void setRegisteringCode(long registeringCode) {
		this.registeringCode = registeringCode;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}