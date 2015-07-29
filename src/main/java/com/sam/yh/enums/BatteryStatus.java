package com.sam.yh.enums;

public enum BatteryStatus {

    NORMAL("0", "正常"), 
    V_ABNORMAL("1", "电压异常"), 
    T_ABNORMAL("2", "温度异常"), 
    LOCKED("3", "电池锁定");

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
