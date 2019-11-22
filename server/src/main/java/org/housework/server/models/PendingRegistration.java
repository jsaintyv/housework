package org.housework.server.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="pending_registration")
public class PendingRegistration {
	// PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User worker;
	   
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id")
    private House target;
	
    private Long secret;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getWorker() {
		return worker;
	}

	public void setWorker(User worker) {
		this.worker = worker;
	}

	public House getTarget() {
		return target;
	}

	public void setTarget(House target) {
		this.target = target;
	}

	public Long getSecret() {
		return secret;
	}

	public void setSecret(Long secret) {
		this.secret = secret;
	}    
}
