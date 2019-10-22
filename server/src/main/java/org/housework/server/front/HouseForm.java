package org.housework.server.front;

import org.housework.server.models.House;

public class HouseForm {	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void copyInto(House house) {
		house.setName(this.name);
	}
}
