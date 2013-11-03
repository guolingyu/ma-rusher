package com.self.mahunter.service;

import java.util.List;

import com.self.mahunter.entity.Card;
import com.self.mahunter.entity.UserState;

public class UserStateService {

	private static UserStateService instance = new UserStateService();

	private UserStateService() {
	};

	public static UserStateService getInstance() {
		return instance;
	}

	private UserState userState;
	
	private List<Card> userCards;

	public UserState getUserState() {
		return userState;
	}

	public void setUserState(UserState userState) {
		this.userState = userState;
	}

	public List<Card> getUserCards() {
		return userCards;
	}

	public void setUserCards(List<Card> userCards) {
		this.userCards = userCards;
	}

}
