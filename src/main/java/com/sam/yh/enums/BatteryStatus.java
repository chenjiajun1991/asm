package com.sam.yh.enums;

public enum BatteryStatus {

	NORMAL(1, "Õı³£"), 
	ABNORMAL(2, "Òì³£");

	private int status;
	private String desc;

	private BatteryStatus(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
