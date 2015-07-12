package com.sam.yh.req.bean;

public class SmsAuthCodeReq {

    private String userPhone;
    private String authType;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

}
