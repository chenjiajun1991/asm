package com.sam.yh.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserCodeType {

    TEST_CODE(0, "测试短信"), 
    SIGNUP_CODE(1, "注册短信验证码"),
    USER_SALT(2, "注册盐"), 
    BTY_SALT(3, "电池盐");
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
