package com.self.mahunter.utils;

import net.sf.json.JSONObject;

public class JSONHelper {

	public static JSONObject buildSuccJSON() {
		JSONObject json = new JSONObject();
		json.put("success", 1);
		return json;
	}

	public static JSONObject buildErrJSON(String error) {
		JSONObject json = new JSONObject();
		json.put("success", 0);
		json.put("error", error);
		return json;
	}
}
