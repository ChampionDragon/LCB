package com.lcb.bean;

public class RecordBean {
	String title;
	String time;
	String type;

	public RecordBean(String title, String time, String type) {
		super();
		this.title = title;
		this.time = time;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
