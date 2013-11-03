package com.self.mahunter.utils;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.self.mahunter.entity.CardData;
import com.self.mahunter.entity.CardDatabase;

public class DuowanDataSpider {

	private static final String[] DUOWAN_CARD_DATA_PAGES = {
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxLCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoyLCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjozLCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo0LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo1LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo2LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo3LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo4LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjo5LCJzb3J0IjoiY29tbW9uTHYxQVRLLmFzYyJ9.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxMCwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxMSwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxMiwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxMywic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxNCwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxNSwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxNiwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxNywic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxOCwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html",
			"http://db.duowan.com/ma/cn/card/list/eyJwIjoxOSwic29ydCI6ImNvbW1vbkx2MUFUSy5hc2MifQ_3__3_.html" };

	public static void main(String[] args) throws IOException {
		CardDatabase database = new CardDatabase();
		database.setRefreshTime(System.currentTimeMillis());
		database.setCardmap(new HashMap<String, CardData>());
		
		for (int i = 0; i < DUOWAN_CARD_DATA_PAGES.length; i++) {
			String url = DUOWAN_CARD_DATA_PAGES[i];
			Document doc = Jsoup.connect(url).get();
			Elements elemtns = doc.select("tr.even");
			for (Element element : elemtns) {
				Element imgnamediv = element.child(1);
				Element imgElem = imgnamediv.select("img").get(0);

				CardData cardData = new CardData();
				cardData.setImgUrl(imgElem.attr("src"));
				cardData.setName(imgElem.attr("title"));
				cardData.setStar(Integer.parseInt(element.child(2).text()));
				String masterIdString = StringUtils.substringBetween(
						imgElem.attr("src"), "face_", ".jpg");
				cardData.setMasterCardId(Integer.parseInt(masterIdString));
				cardData.setCost(Integer.parseInt(element.child(3).text()));
				cardData.setGroup(element.child(4).text());

				System.out.println(cardData);
				database.getCardmap().put(cardData.getMasterCardId().toString(),
						cardData);
			}
		}
		
		database.setTotal(database.getCardmap().size());
		
		String json = database.toJSONString();
		System.out.println(json);
	}

}
