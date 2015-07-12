package com.sam.yh.req.bean;

public class SysSaltReq {

    private String userName;
    private String saltType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSaltType() {
        return saltType;
    }

    public void setSaltType(String saltType) {
        this.saltType = saltType;
    }

}
