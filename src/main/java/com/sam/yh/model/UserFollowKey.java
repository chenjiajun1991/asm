package com.sam.yh.model;

public class UserFollowKey {
    private Integer userId;

    private Integer followBatteryId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFollowBatteryId() {
        return followBatteryId;
    }

    public void setFollowBatteryId(Integer followBatteryId) {
        this.followBatteryId = followBatteryId;
    }
}