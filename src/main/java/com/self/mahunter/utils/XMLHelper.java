package com.self.mahunter.utils;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XMLHelper {

	public static String getSingleNodeAsString(Document document, String path) {
		Node node = document.selectSingleNode(path);
		if (null == node) {
			return null;
		} else {
			return node.getText();
		}
	}

	public static void main(String[] args) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader
					.read(new File(
							"D:\\workspace\\liveshow\\ma-hunter\\xmlsample\\login_result.xml"));
			Node node = document
					.selectSingleNode("/response/header/error/code");
			if (node != null) {
				Element element = (Element) node;
				String code = element.getText();
				System.out.println(code);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
