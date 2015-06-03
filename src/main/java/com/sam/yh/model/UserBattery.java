package com.sam.yh.model;

import java.util.Date;

public class UserBattery extends UserBatteryKey {
    private Date buyDate;

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
}