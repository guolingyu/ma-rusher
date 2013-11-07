package com.self.mahunter.entity;

import java.util.List;

public class BattleRule {
	
	private Integer minHp;
	
	private Integer maxHp;
	
	private Integer minLv;
	
	private Integer maxLv;
	
	private String cards;

	public String getCards() {
		return cards;
	}

	public void setCards(String cards) {
		this.cards = cards;
	}

	public Integer getMinHp() {
		return minHp;
	}

	public void setMinHp(Integer minHp) {
		this.minHp = minHp;
	}

	public Integer getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(Integer maxHp) {
		this.maxHp = maxHp;
	}

	public Integer getMinLv() {
		return minLv;
	}

	public void setMinLv(Integer minLv) {
		this.minLv = minLv;
	}

	public Integer getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(Integer maxLv) {
		this.maxLv = maxLv;
	}
	
}
