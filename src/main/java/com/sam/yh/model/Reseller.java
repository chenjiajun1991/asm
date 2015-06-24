package com.sam.yh.model;

import java.util.Date;

public class Reseller {
    private Integer userId;

    private String officeAddress;

    private String emailAddress;

    private Byte emailAddressVerified;

    private Integer verifyStatus;

    private String cityName;

    private Integer cityId;

    private Date verifyDate;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress == null ? null : officeAddress.trim();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress == null ? null : emailAddress.trim();
    }

    public Byte getEmailAddressVerified() {
        return emailAddressVerified;
    }

    public void setEmailAddressVerified(Byte emailAddressVerified) {
        this.emailAddressVerified = emailAddressVerified;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName == null ? null : cityName.trim();
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }
}