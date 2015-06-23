package com.sam.yh.req.bean;

public class LogResellerReq extends BaseReq {

    private String resellerName;
    private String resellerPhone;
    private String resellerEmail;
    private String resellerAddress;

    public String getResellerName() {
        return resellerName;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public String getResellerPhone() {
        return resellerPhone;
    }

    public void setResellerPhone(String resellerPhone) {
        this.resellerPhone = resellerPhone;
    }

    public String getResellerEmail() {
        return resellerEmail;
    }

    public void setResellerEmail(String resellerEmail) {
        this.resellerEmail = resellerEmail;
    }

    public String getResellerAddress() {
        return resellerAddress;
    }

    public void setResellerAddress(String resellerAddress) {
        this.resellerAddress = resellerAddress;
    }

}
