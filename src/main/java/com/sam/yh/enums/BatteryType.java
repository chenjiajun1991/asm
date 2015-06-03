package com.sam.yh.enums;

public enum BatteryType {

	Normal_BATTERY(0, "∆’Õ®µÁ≥ÿ"), 
	CLOUD_BATTERY(1, "‘∆µÁ≥ÿ");

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
