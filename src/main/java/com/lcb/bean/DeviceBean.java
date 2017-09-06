package com.lcb.bean;

public class DeviceBean {
	private int id;
	private long creattime;
	private String deviceControl;
	private String deviceName;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getCreattime() {
		return creattime;
	}

	public void setCreattime(long creattime) {
		this.creattime = creattime;
	}

	public DeviceBean(long creattime, String deviceControl, String deviceName) {
		super();
		this.creattime = creattime;
		this.deviceControl = deviceControl;
		this.deviceName = deviceName;
	}

	public DeviceBean() {

	}

	public String getDeviceControl() {
		return deviceControl;
	}

	public void setDeviceControl(String deviceControl) {
		this.deviceControl = deviceControl;
	}
}
