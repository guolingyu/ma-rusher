package com.self.mahunter.entity;

public class UserState {

	private String name;

	private String userId;

	private int currentAP;

	private int maxAP;

	private int currentBC;

	private int maxBC;

	private long refreshTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentAP() {
		return currentAP;
	}

	public void setCurrentAP(int currentAP) {
		this.currentAP = currentAP;
	}

	public int getMaxAP() {
		return maxAP;
	}

	public void setMaxAP(int maxAP) {
		this.maxAP = maxAP;
	}

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getCurrentBC() {
		return currentBC;
	}

	public void setCurrentBC(int currentBC) {
		this.currentBC = currentBC;
	}

	public int getMaxBC() {
		return maxBC;
	}

	public void setMaxBC(int maxBC) {
		this.maxBC = maxBC;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserState [name=" + name + ", userId=" + userId
				+ ", currentAP=" + currentAP + ", maxAP=" + maxAP
				+ ", currentBC=" + currentBC + ", maxBC=" + maxBC
				+ ", refreshTime=" + refreshTime + "]";
	}

}
