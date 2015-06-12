package com.sam.yh.enums;

public enum BatteryType {

	NOEMAL_BATTERY(0, "普通蓄电池"), 
	CLOUD_BATTERY(1, "云电池");

	private int type;
	private String desc;

	private BatteryType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
