package com.sam.yh.req.bean;

public class SubmitBtySpecReq extends BaseReq {

    private String userName;
    private String userPhone;
    private String btySN;
    private String btyImei;
    private String btySimNo;
    private String resellerEmail;

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

    public String getBtySN() {
        return btySN;
    }

    public void setBtySN(String btySN) {
        this.btySN = btySN;
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

    public String getResellerEmail() {
        return resellerEmail;
    }

    public void setResellerEmail(String resellerEmail) {
        this.resellerEmail = resellerEmail;
    }

}
