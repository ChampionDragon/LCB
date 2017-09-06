package com.lcb.bean;

import java.util.List;


public class DeviceManagerBean {
	String data;
	List<DeviceBean> list;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<DeviceBean> getList() {
		return list;
	}

	public DeviceManagerBean(String data, List<DeviceBean> list) {
		super();
		this.data = data;
		this.list = list;
	}

	public void setList(List<DeviceBean> list) {
		this.list = list;
	}
}
