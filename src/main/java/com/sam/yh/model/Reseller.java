package com.sam.yh.model;

import java.util.Date;

public class Reseller {
    private Integer resellerId;

    private String uuid;

    private String resellerName;

    private String servicePhone;

    private String officeAddress;

    private String emailAddress;

    private Byte emailAddressVerified;

    private String salt;

    private String password;

    private Integer status;

    private Date loggingDate;

    private Date loginDate;

    public Integer getResellerId() {
        return resellerId;
    }

    public void setResellerId(Integer resellerId) {
        this.resellerId = resellerId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public String getResellerName() {
        return resellerName;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName == null ? null : resellerName.trim();
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone == null ? null : servicePhone.trim();
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getLoggingDate() {
        return loggingDate;
    }

    public void setLoggingDate(Date loggingDate) {
        this.loggingDate = loggingDate;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}