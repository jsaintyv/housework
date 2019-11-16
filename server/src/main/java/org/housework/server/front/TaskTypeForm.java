package org.housework.server.front;

import org.housework.server.models.TaskType;

public class TaskTypeForm {
	private String name;
	
	private double value;

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
	}	
}
