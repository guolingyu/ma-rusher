package com.self.mahunter.utils;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

public class MAApiResult {

	private int error;

	private String errorMessage;

	private Document data;

	public MAApiResult() {
		super();
	}

	public MAApiResult(int error, String errorMessage, Document data) {
		super();
		this.error = error;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Document getData() {
		return data;
	}

	public void setData(Document data) {
		this.data = data;
	}

	public String query(String xpath) {
		return data.selectSingleNode(xpath).getText();
	}

	@SuppressWarnings("unchecked")
	public List<Node> queryList(String xpath) {
		return data.selectNodes(xpath);
	}
}
