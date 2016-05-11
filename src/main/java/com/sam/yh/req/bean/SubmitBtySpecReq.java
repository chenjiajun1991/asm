package com.sam.yh.req.bean;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SubmitBtySpecReq implements Serializable {

    private static final long serialVersionUID = -115181405613943874L;
    private String userName;
    private String userPhone;
    private String btySN;
    private String btyImei;
    private String btySimNo;
    private String resellerPhone;
    private Integer btyQuantity;

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

    public String getResellerPhone() {
        return resellerPhone;
    }

    public void setResellerPhone(String resellerPhone) {
        this.resellerPhone = resellerPhone;
    }

    public Integer getBtyQuantity() {
        return btyQuantity;
    }

    public void setBtyQuantity(Integer btyQuantity) {
        this.btyQuantity = btyQuantity;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
