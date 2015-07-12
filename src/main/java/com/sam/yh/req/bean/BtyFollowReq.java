package com.sam.yh.req.bean;

public class BtyFollowReq {

    private String userPhone;
    private String btyPubSn;
    private String btyOwnerPhone;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getBtyOwnerPhone() {
        return btyOwnerPhone;
    }

    public void setBtyOwnerPhone(String btyOwnerPhone) {
        this.btyOwnerPhone = btyOwnerPhone;
    }

    public String getBtyPubSn() {
        return btyPubSn;
    }

    public void setBtyPubSn(String btyPubSn) {
        this.btyPubSn = btyPubSn;
    }

}
