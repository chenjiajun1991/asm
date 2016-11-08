package com.sam.yh.model;

import java.util.Date;

public class TroubleUserBattery {
	 private Integer batteryId;
	 
	 private Integer userId;
	 
	 private Date removeDate;

	public Integer getBatteryId() {
		return batteryId;
	}

	public void setBatteryId(Integer batteryId) {
		this.batteryId = batteryId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}
}
