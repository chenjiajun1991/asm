package com.sam.yh.model;

import java.util.Date;

public class Battery {
    private Integer id;

    private String sn;

    private String status;

    private Boolean btyType;

    private String pubSn;

    private String imei;

    private String simNo;

    private String imsi;

    private String gsmSimNo;

    private String iccid;

    private Integer resellerId;

    private Boolean saleStatus;

    private Integer cityId;

    private String lockLongitude;

    private String lockLatitude;

    private Date createDate;

    private Date saleDate;

    private Date lockDate;

    private Date expiryDate;

    private Integer btyQuantity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Boolean getBtyType() {
        return btyType;
    }

    public void setBtyType(Boolean btyType) {
        this.btyType = btyType;
    }

    public String getPubSn() {
        return pubSn;
    }

    public void setPubSn(String pubSn) {
        this.pubSn = pubSn == null ? null : pubSn.trim();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei == null ? null : imei.trim();
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo == null ? null : simNo.trim();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getGsmSimNo() {
        return gsmSimNo;
    }

    public void setGsmSimNo(String gsmSimNo) {
        this.gsmSimNo = gsmSimNo == null ? null : gsmSimNo.trim();
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid == null ? null : iccid.trim();
    }

    public Integer getResellerId() {
        return resellerId;
    }

    public void setResellerId(Integer resellerId) {
        this.resellerId = resellerId;
    }

    public Boolean getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(Boolean saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getLockLongitude() {
        return lockLongitude;
    }

    public void setLockLongitude(String lockLongitude) {
        this.lockLongitude = lockLongitude == null ? null : lockLongitude.trim();
    }

    public String getLockLatitude() {
        return lockLatitude;
    }

    public void setLockLatitude(String lockLatitude) {
        this.lockLatitude = lockLatitude == null ? null : lockLatitude.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public void setLockDate(Date lockDate) {
        this.lockDate = lockDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getBtyQuantity() {
        return btyQuantity;
    }

    public void setBtyQuantity(Integer btyQuantity) {
        this.btyQuantity = btyQuantity;
    }

}