package com.sam.yh.req.bean;

import java.io.Serializable;

public class ListFollowersReq implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userPhone;
    private String btyPubSn;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getBtyPubSn() {
        return btyPubSn;
    }

    public void setBtyPubSn(String btyPubSn) {
        this.btyPubSn = btyPubSn;
    }

}
