package com.sam.yh.req.bean.web;

public class UpdateBatteryReq {
	private int btyId;
	private String btyImei;
	private String btySimNo;
	private String btySn;
	private int btyQuantity;
	public int getBtyId() {
		return btyId;
	}
	public void setBtyId(int btyId) {
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
	public int getBtyQuantity() {
		return btyQuantity;
	}
	public void setBtyQuantity(int btyQuantity) {
		this.btyQuantity = btyQuantity;
	}
	

}
