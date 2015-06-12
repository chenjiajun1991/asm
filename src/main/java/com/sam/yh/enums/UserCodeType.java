package com.sam.yh.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserCodeType {

    BTY_SALT(1, "电池盐"),
    USER_SALT(2, "注册盐"), 
    SIGNUP_CODE(3, "注册短信验证码");
    private static List<Integer> types = new ArrayList<Integer>();

    static {
        for (UserCodeType type : UserCodeType.values()) {
            types.add(type.getType());
        }
    }

    private int type;
    private String desc;

    private UserCodeType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static boolean isValidType(int type) {
        return types.contains(type);
    }

}
