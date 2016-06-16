package com.sam.yh.model;

import java.util.Date;

public class UserBatteryInfo {
	private Integer userId;
	private Integer batteryId;
	private Date buyDate;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getBatteryId() {
		return batteryId;
	}
	public void setBatteryId(Integer batteryId) {
		this.batteryId = batteryId;
	}
	public Date getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(Date buyDate) {
		this.buyDate = buyDate;
	}
	
	 
}
