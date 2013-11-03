package com.self.mahunter.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.self.mahunter.entity.CardGroupItem;
import com.self.mahunter.entity.CardGroupSetting;

public class CardGroupSettingService {

	public static final String filePath = System.getProperty("user.dir")
			+ "/data/card-settings.json";

	private static CardGroupSettingService instance = new CardGroupSettingService();

	private CardGroupSettingService() {
		super();
	}

	public static CardGroupSettingService getInstance() {
		return instance;
	}

	private Map<String, List<CardGroupItem>> groupSettings;

	private ObjectMapper objectMapper = new ObjectMapper();

	public void addOrUpdate(CardGroupSetting setting) throws IOException {
		groupSettings.put(setting.getGroupId(), setting.getCards());
		save();
	}

	public CardGroupSetting get(String groupId) {
		List<CardGroupItem> cards = groupSettings.get(groupId);
		if (cards == null) {
			return null;
		}
		CardGroupSetting setting = new CardGroupSetting();
		setting.setGroupId(groupId);
		setting.setCards(cards);
		return setting;
	}

	private void save() throws IOException {
		String groupSettingsText = objectMapper
				.writeValueAsString(groupSettings);
		FileUtils.writeStringToFile(new File(filePath), groupSettingsText);
	}

	private void load() throws IOException {
		String json = FileUtils.readFileToString(new File(filePath));
		if (StringUtils.isNotBlank(json)) {
			groupSettings = objectMapper.readValue(json, Map.class);
		}
	}
}
