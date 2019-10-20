package org.housework.server.models;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {

    // PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login", unique=true)
    private String login;
    
    private String password;
    
    
    public User() {}
    
    public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}