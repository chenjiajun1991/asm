package com.sam.yh.model;

import java.util.Date;

public class TroubleBattery {
	private Integer batteryId;
	private String imei;
	private String simNo;
	private String sn;
	private Integer btyQuantity;
	private Integer resellerId;
	private Date saleDate;
	private Date removeDate;
	private String reason;
	
	public Integer getBatteryId() {
		return batteryId;
	}
	public void setBatteryId(Integer batteryId) {
		this.batteryId = batteryId;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getBtyQuantity() {
		return btyQuantity;
	}
	public void setBtyQuantity(Integer btyQuantity) {
		this.btyQuantity = btyQuantity;
	}
	public Integer getResellerId() {
		return resellerId;
	}
	public void setResellerId(Integer resellerId) {
		this.resellerId = resellerId;
	}
	
	public Date getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	public Date getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
