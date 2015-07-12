package com.sam.yh.enums;

public enum AppVersionStatus {

    NO_UPDATE("N", "无更新"), 
    OPTIONAL_UPDATE("O", "非强制更新"), 
    FORCE_UPDATE("F", "强制更新");

    private String status;
    private String desc;

    private AppVersionStatus(String status, String desc) {
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
