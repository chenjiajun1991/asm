package com.sam.yh.model;

import java.util.Date;

public class UserBattery extends UserBatteryKey {

    private String btyPubSn;
    private String bytImei;
    private Date buyDate;

    public String getBtyPubSn() {
        return btyPubSn;
    }

    public void setBtyPubSn(String btyPubSn) {
        this.btyPubSn = btyPubSn;
    }

    public String getBytImei() {
        return bytImei;
    }

    public void setBytImei(String bytImei) {
        this.bytImei = bytImei;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

	@Override
	public String toString() {
		return "UserBattery [btyPubSn=" + btyPubSn + ", bytImei=" + bytImei
				+ ", buyDate=" + buyDate + "]";
	}
    
    

}