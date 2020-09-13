package org.housework.server.front;

import org.housework.server.models.TaskType;

public class TaskTypeForm {
	private String name;	
	private double value;
    private String colorRgb;    
    private String shortName;
	private Boolean reservedToAdmin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void copyInto(TaskType type) {
		type.setName(this.name);		
		type.setValue(this.value);
		type.setColorRgb(this.colorRgb);
		
		type.setShortName(this.shortName);
		type.setReservedToAdmin(getReservedToAdmin());
	}

	public String getColorRgb() {
		return colorRgb;
	}

	public void setColorRgb(String colorRgb) {
		this.colorRgb = colorRgb;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}	
	
	public Boolean getReservedToAdmin() {
		return reservedToAdmin;
	}
	
	public void setReservedToAdmin(Boolean reservedToAdmin) {
		this.reservedToAdmin = reservedToAdmin;
	}
}
