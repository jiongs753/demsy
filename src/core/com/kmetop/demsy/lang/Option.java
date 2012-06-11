package com.kmetop.demsy.lang;

public class Option {
	private String value;

	private String text;

	public Option() {
	}

	public Option(String value, String text) {
		this.text = text;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String key) {
		this.text = key;
	}
}
