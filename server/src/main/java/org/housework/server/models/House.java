package org.housework.server.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="houses")
public class House {

	// PrimaryKey
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String name;

    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;
    
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "house_id")
    private Set<TaskType> types = new HashSet<TaskType>();
    
    
    @ManyToMany(
    fetch = FetchType.EAGER,
   	cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name = "user_affected_house",
        joinColumns = @JoinColumn(name = "house_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<User> getUsers() {
		return users;
	}
	
	public Set<TaskType> getTypes() {
		return types;
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
		House other = (House) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public boolean accept(User user) {
		if(user == null) {
			return false;
		}
		
		return user.getId() == owner.getId() || getUsers().contains(user);
	}
	
	public boolean isAdmin(User user) {
		if(user == null) {
			return false;
		}
		
		return user.getId() == owner.getId();
	}
}
