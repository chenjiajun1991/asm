package com.sam.yh.enums;

public enum BatteryStatus {

    NORMAL("1", "正常"), 
    ABNORMAL("2", "异常");

    private String status;
    private String desc;

    private BatteryStatus(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
