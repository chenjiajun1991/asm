package com.sam.yh.req.bean;

public class BtyShareReq extends BaseReq{
    
    private String userPhone;
    private String btyPubSn;
    private String friendPhone;
    
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
    public String getFriendPhone() {
        return friendPhone;
    }
    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }
    
    
}
