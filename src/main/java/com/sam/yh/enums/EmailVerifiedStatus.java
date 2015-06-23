package com.sam.yh.enums;

public enum EmailVerifiedStatus {
    NOT_VERIFIED(0, "未验证"),
    VERIFIED(1, "已验证");

    private byte verifyStatus;
    private String desc;

    private EmailVerifiedStatus(int verifyStatus, String desc) {
        this.verifyStatus = (byte) verifyStatus;
        this.desc = desc;
    }

    public byte getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(byte verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
