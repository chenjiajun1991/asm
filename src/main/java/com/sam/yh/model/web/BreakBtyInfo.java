package com.sam.yh.model.web;

public class BreakBtyInfo {
	
	private Integer btyId;
	 private String btyImei;
	 private String btySimNo;
	 private String btySn;
     private Integer btyQuantity;
     private String longitude;
	 private String latitude;
	 private String voltage;
	 private String temperature;
	 private String msg;
	 private String receiveDate;
	 private String userName;
	 private String userphone;
	 private String resellerName;
	 private String resellerPhone; 
	 private String saleDate;
	public Integer getBtyId() {
		return btyId;
	}
	public void setBtyId(Integer btyId) {
		this.btyId = btyId;
	}
	public String getBtyImei() {
		return btyImei;
	}
	public void setBtyImei(String btyImei) {
		this.btyImei = btyImei;
	}
	public String getBtySimNo() {
		return btySimNo;
	}
	public void setBtySimNo(String btySimNo) {
		this.btySimNo = btySimNo;
	}
	public String getBtySn() {
		return btySn;
	}
	public void setBtySn(String btySn) {
		this.btySn = btySn;
	}
	public Integer getBtyQuantity() {
		return btyQuantity;
	}
	public void setBtyQuantity(Integer btyQuantity) {
		this.btyQuantity = btyQuantity;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserphone() {
		return userphone;
	}
	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}
	public String getResellerName() {
		return resellerName;
	}
	public void setResellerName(String resellerName) {
		this.resellerName = resellerName;
	}
	public String getResellerPhone() {
		return resellerPhone;
	}
	public void setResellerPhone(String resellerPhone) {
		this.resellerPhone = resellerPhone;
	}
	public String getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}
	
	
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	@Override
	public String toString() {
		return "BreakBtyInfo [btyId=" + btyId + ", btyImei=" + btyImei
				+ ", btySimNo=" + btySimNo + ", btySn=" + btySn
				+ ", btyQuantity=" + btyQuantity + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", voltage=" + voltage
				+ ", temperature=" + temperature + ", msg=" + msg
				+ ", receiveDate=" + receiveDate + ", userName=" + userName
				+ ", userphone=" + userphone + ", resellerName=" + resellerName
				+ ", resellerPhone=" + resellerPhone + ", saleDate=" + saleDate
				+ "]";
	}
	
}
