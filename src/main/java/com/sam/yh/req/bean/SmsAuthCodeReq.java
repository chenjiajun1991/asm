package com.sam.yh.req.bean;

public class SmsAuthCodeReq extends BaseReq {

    private String userName;
    private String authType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

}
