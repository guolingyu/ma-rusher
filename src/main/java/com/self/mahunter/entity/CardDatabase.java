package com.self.mahunter.entity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CardDatabase {

	private long refreshTime;

	private int total;

	private Map<String, CardData> cardmap;

	public long getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Map<String, CardData> getCardmap() {
		return cardmap;
	}

	public void setCardmap(Map<String, CardData> cardmap) {
		this.cardmap = cardmap;
	}

	public String toJSONString() {
		return JSONObject.fromObject(this).toString();
	}

	public static CardDatabase fromJSONString(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, CardDatabase.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		CardDatabase database = new CardDatabase();
		database.setRefreshTime(System.currentTimeMillis());
		CardData cardData = new CardData();
		cardData.setName("第二型马格纳柏林");
		cardData.setMasterCardId(54);
		cardData.setStar(1);
		database.setCardmap(new HashMap<String, CardData>());
		database.getCardmap().put(cardData.getMasterCardId().toString(),
				cardData);
		database.setTotal(database.getCardmap().size());
		String json = database.toJSONString();
		System.out.println(json);

		CardDatabase database2 = CardDatabase.fromJSONString(json);

	}
}
