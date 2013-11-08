package com.self.mahunter.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.self.mahunter.entity.BattleRule;
import com.self.mahunter.entity.Fairy;

public class BattleIntelligentService {

	private static BattleIntelligentService instance = new BattleIntelligentService();

	public static BattleIntelligentService getInstance() {
		return instance;
	}

	private BattleIntelligentService() {
		String filePath = System.getProperty("user.dir")
				+ "/data/battle-rule.json";
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		try {
			String json = FileUtils.readFileToString(file);
			if (StringUtils.isBlank(json)) {
				return;
			}
			JSONObject ob = JSONObject.fromObject(json);
			if (null != ob.getJSONArray("rules")) {
				rules = JSONArray.toList(ob.getJSONArray("rules"),
						BattleRule.class);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

	private List<BattleRule> rules;

	public String getCards(Fairy fairy) {
		if (null == fairy || null == fairy.getHp() || null == fairy.getLevel()) {
			return null;
		}
		BattleRule adopted = null;
		for (int i = 0; i < rules.size(); i++) {
			BattleRule rule = rules.get(i);
			if (null != rule.getMinHp() && rule.getMinHp() > fairy.getHp()) {
				continue;
			}
			if (null != rule.getMaxHp() && rule.getMaxHp() < fairy.getHp()) {
				continue;
			}
			if (null != rule.getMaxLv() && rule.getMaxLv() < fairy.getLevel()) {
				continue;
			}
			if (null != rule.getMinLv() && rule.getMinLv() > fairy.getLevel()) {
				continue;
			}
			adopted = rule;
			break;
		}
		if (null != adopted) {
			return adopted.getCards();
		}
		return null;
	}

	public void addRule(BattleRule rule) {
		rules.add(rule);
		save();
	}

	public void save() {
		JSONObject json = new JSONObject();
		json.put("rules", rules);
		String filePath = System.getProperty("user.dir")
				+ "/data/battle-rule.json";
		try {
			FileUtils.writeStringToFile(new File(filePath), json.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BattleIntelligentService service = BattleIntelligentService
				.getInstance();
		Fairy fairy = new Fairy();
		fairy.setHp(2000);
		fairy.setLevel(10);
		System.out.println(service.getCards(fairy));
	}
}
