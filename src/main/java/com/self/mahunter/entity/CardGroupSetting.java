package com.self.mahunter.entity;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CardGroupSetting {

	private String groupId;

	private List<CardGroupItem> cards;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<CardGroupItem> getCards() {
		return cards;
	}

	public void setCards(List<CardGroupItem> cards) {
		this.cards = cards;
	}

	public String toJSONString() {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(this);
		} catch (IOException e) {
			return null;
		}
	}
}
