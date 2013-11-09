package com.self.mahunter.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.self.mahunter.entity.CardData;
import com.self.mahunter.entity.CardDatabase;

public class CardDatabaseService {

	private CardDatabase database;

	private boolean loaded = false;

	private CardDatabaseService() {
		String filePath = System.getProperty("user.dir")
				+ "/data/card-data.json";
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		try {
			String json = FileUtils.readFileToString(file);
			database = CardDatabase.fromJSONString(json);
			System.out.println("database loaded:" + database);
			loaded = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private static CardDatabaseService instance = new CardDatabaseService();

	public static CardDatabaseService getInstance() {
		return instance;
	}

	public CardDatabase getCardDatabase() {
		return database;
	}

	public CardData getCardData(String serialId) {
		return database.getCardmap().get(serialId);
	}

	public static void main(String[] args) {
		CardDatabaseService cardDatabaseService = CardDatabaseService
				.getInstance();
		System.out.println(cardDatabaseService.getCardDatabase().getCardmap()
				.get("190"));
	}
}
