package com.sam.yh.req.bean;

public class BatteryInfoReq {

    private String imei;
    private String longitude;
    private String latitude;
    private String temperature;
    private String voltage;
    private String sampleDate;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(String sampleDate) {
        this.sampleDate = sampleDate;
    }

    @Override
    public String toString() {
        return "BatteryInfoReqVo [imei=" + imei + ", longitude=" + longitude + ", latitude=" + latitude + ", temperature=" + temperature + ", voltage="
                + voltage + ", sampleDate=" + sampleDate + "]";
    }

}