package com.sam.yh.req.bean.web;

public class UpdateUserBatteryReq {
   private int btyId;
   private String userPhone;
   private String userName;
public int getBtyId() {
	return btyId;
}
public void setBtyId(int btyId) {
	this.btyId = btyId;
}
public String getUserPhone() {
	return userPhone;
}
public void setUserPhone(String userPhone) {
	this.userPhone = userPhone;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
}
