package com.sam.yh.enums;

public enum UserType {

    NORMAL_USER("0", "普通用户"),
    RESELLER("1", "经销商"), 
    ADMIN("2", "管理员");

    private String type;
    private String desc;

    private UserType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
