package com.self.mahunter.entity;

public class Card {

	private Integer seriald;

	private Integer masterCardId;

	private Integer hp;

	private Integer power;

	private Integer lv;

	private Integer lvMax;

	private Integer limitOver;

	private Integer plusLimitCount;

	private String name;

	private String imgUrl;

	private Integer star;

	private Integer cost;

	public Integer getSeriald() {
		return seriald;
	}

	public void setSeriald(Integer seriald) {
		this.seriald = seriald;
	}

	public Integer getMasterCardId() {
		return masterCardId;
	}

	public void setMasterCardId(Integer masterCardId) {
		this.masterCardId = masterCardId;
	}

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public Integer getLvMax() {
		return lvMax;
	}

	public void setLvMax(Integer lvMax) {
		this.lvMax = lvMax;
	}

	public Integer getLimitOver() {
		return limitOver;
	}

	public void setLimitOver(Integer limitOver) {
		this.limitOver = limitOver;
	}

	public Integer getPlusLimitCount() {
		return plusLimitCount;
	}

	public void setPlusLimitCount(Integer plusLimitCount) {
		this.plusLimitCount = plusLimitCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Card [seriald=" + seriald + ", masterCardId=" + masterCardId
				+ ", hp=" + hp + ", power=" + power + ", lv=" + lv + ", lvMax="
				+ lvMax + ", limitOver=" + limitOver + ", plusLimitCount="
				+ plusLimitCount + ", name=" + name + ", imgUrl=" + imgUrl
				+ ", star=" + star + ", cost=" + cost + "]";
	}
}
