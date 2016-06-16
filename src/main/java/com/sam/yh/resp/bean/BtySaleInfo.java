package com.sam.yh.resp.bean;

import java.util.Date;

public class BtySaleInfo {
	 private String btyImei;
	 private String btySimNo;
	 private String btySn;
	 private String userName;
	 private String userphone;
	 private String resellerName;
	 private String resellerPhone;
	 private String saleDate;
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

	
}
