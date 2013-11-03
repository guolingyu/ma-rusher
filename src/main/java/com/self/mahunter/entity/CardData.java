package com.self.mahunter.entity;

public class CardData {

	private Integer masterCardId;

	private String name;

	private Integer star;

	private String imgUrl;
	
	private Integer cost;
	
	private String group;

	public Integer getMasterCardId() {
		return masterCardId;
	}
	
	public void setMasterCardId(Integer masterCardId) {
		this.masterCardId = masterCardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return "CardData [masterCardId=" + masterCardId + ", name=" + name
				+ ", star=" + star + ", imgUrl=" + imgUrl + ", cost=" + cost
				+ ", group=" + group + "]";
	}
}
