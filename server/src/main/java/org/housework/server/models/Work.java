package org.housework.server.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="works")
public class Work {
	// PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    Date date;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User worker;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id")
    private House house;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getWorker() {
		return worker;
	}

	public void setWorker(User worker) {
		this.worker = worker;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
}
