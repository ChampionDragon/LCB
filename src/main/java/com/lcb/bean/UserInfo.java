package com.lcb.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserInfo implements Serializable {
	String name;
	String SSID;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSSID() {
		return SSID;
	}

	public void setSSID(String sSID) {
		SSID = sSID;
	}
}
