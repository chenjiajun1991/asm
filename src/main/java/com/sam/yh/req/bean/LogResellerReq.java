package com.sam.yh.req.bean;

public class LogResellerReq extends BaseReq {

    private String adminPhone;
    private String resellerName;
    private String resellerPhone;
    private String cityName;
    private String resellerAddress;

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getResellerAddress() {
        return resellerAddress;
    }

    public void setResellerAddress(String resellerAddress) {
        this.resellerAddress = resellerAddress;
    }

}
