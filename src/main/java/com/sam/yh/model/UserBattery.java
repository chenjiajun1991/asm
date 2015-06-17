package com.sam.yh.model;

import java.util.Date;

public class UserBattery extends UserBatteryKey {

    private String btyPubSn;
    private Date buyDate;

    public String getBtyPubSn() {
        return btyPubSn;
    }

    public void setBtyPubSn(String btyPubSn) {
        this.btyPubSn = btyPubSn;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}