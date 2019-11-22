package org.housework.server.front;

public class WorkForm {
	private long date;
	private int workerId;
	private int taskTypeId;
	
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getWorkerId() {
		return workerId;
	}
	public void setWorkerId(int workerId) {
		this.workerId = workerId;
	}
	public int getTaskTypeId() {
		return taskTypeId;
	}
	public void setTaskTypeId(int taskTypeId) {
		this.taskTypeId = taskTypeId;
	}
}
