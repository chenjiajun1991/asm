package com.sam.yh.req.bean.vo;

public class SubmitBatteryReqVo extends BaseReqVo {

	private String userName;
	private String userPhone;
	private String batterySN;
	private String batteryType;
	private String imei;
	private String simNo;
	private String reseller;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getBatterySN() {
		return batterySN;
	}

	public void setBatterySN(String batterySN) {
		this.batterySN = batterySN;
	}

	public String getBatteryType() {
		return batteryType;
	}

	public void setBatteryType(String batteryType) {
		this.batteryType = batteryType;
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

	public String getReseller() {
		return reseller;
	}

	public void setReseller(String reseller) {
		this.reseller = reseller;
	}

}
