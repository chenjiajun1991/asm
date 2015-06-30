package com.sam.yh.resp.bean;

public class ResellerInfo {

    private int resellerId;
    private String resellerName;
    private String resellerPhone;
    private String resellerProvince;
    private int provinceId;
    private String resellerCity;
    private int cityId;
    private String resellerAddress;
    private String longitude;
    private String latitude;

    public int getResellerId() {
        return resellerId;
    }

    public void setResellerId(int resellerId) {
        this.resellerId = resellerId;
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

    public String getResellerCity() {
        return resellerCity;
    }

    public void setResellerCity(String resellerCity) {
        this.resellerCity = resellerCity;
    }

    public String getResellerAddress() {
        return resellerAddress;
    }

    public void setResellerAddress(String resellerAddress) {
        this.resellerAddress = resellerAddress;
    }

    public String getResellerProvince() {
        return resellerProvince;
    }

    public void setResellerProvince(String resellerProvince) {
        this.resellerProvince = resellerProvince;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
