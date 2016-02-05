package com.sam.yh.req.bean;

import java.io.Serializable;

public class BtyFollowReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userPhone;
    private String btyPubSn;
    private String btyOwnerPhone;
    private String friendNickName;

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

    public String getFriendNickName() {
        return friendNickName;
    }

    public void setFriendNickName(String friendNickName) {
        this.friendNickName = friendNickName;
    }

}
