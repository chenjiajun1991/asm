package com.sam.yh.enums;

public enum ResellerStatus {

    INIT(0, "初始添加"),
    VERIFIED(1, "已认证");

    private int status;
    private String desc;

    private ResellerStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
