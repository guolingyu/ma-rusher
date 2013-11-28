package com.self.mahunter.entity;

public class PVPUser {

	private int userId;

	private String name;

	private int rank;

	private int cost;
	
	private int leadCardMasterId;

	private String leaderCardName;

	private int leaderCardLv;

	private int leaderCardHp;

	private int leaderCardStar;

	private int leaderCardCost;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getLeaderCardName() {
		return leaderCardName;
	}

	public void setLeaderCardName(String leaderCardName) {
		this.leaderCardName = leaderCardName;
	}

	public int getLeaderCardLv() {
		return leaderCardLv;
	}

	public void setLeaderCardLv(int leaderCardLv) {
		this.leaderCardLv = leaderCardLv;
	}

	public int getLeaderCardHp() {
		return leaderCardHp;
	}

	public void setLeaderCardHp(int leaderCardHp) {
		this.leaderCardHp = leaderCardHp;
	}

	public int getLeaderCardStar() {
		return leaderCardStar;
	}

	public void setLeaderCardStar(int leaderCardStar) {
		this.leaderCardStar = leaderCardStar;
	}

	public int getLeaderCardCost() {
		return leaderCardCost;
	}

	public void setLeaderCardCost(int leaderCardCost) {
		this.leaderCardCost = leaderCardCost;
	}

	public int getLeadCardMasterId() {
		return leadCardMasterId;
	}

	public void setLeadCardMasterId(int leadCardMasterId) {
		this.leadCardMasterId = leadCardMasterId;
	}

	@Override
	public String toString() {
		return "PVPUser [userId=" + userId + ", name=" + name + ", rank="
				+ rank + ", cost=" + cost + ", leadCardMasterId="
				+ leadCardMasterId + ", leaderCardName=" + leaderCardName
				+ ", leaderCardLv=" + leaderCardLv + ", leaderCardHp="
				+ leaderCardHp + ", leaderCardStar=" + leaderCardStar
				+ ", leaderCardCost=" + leaderCardCost + "]";
	}
	
	

}
