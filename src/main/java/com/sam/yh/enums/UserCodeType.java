package com.sam.yh.enums;

import java.util.ArrayList;
import java.util.List;

public enum UserCodeType {

    TEST_CODE(0, "测试短信"), 
    SIGNUP_CODE(1, "注册短信验证码"), 
    RESETPWD_CODE(2, "重置密码验证码"), 
    USER_SALT(3, "注册盐"), 
    BTY_SALT(4, "电池盐"),
    BTY_WARNING(5, "异常警告"),
    BTY_MOVING(6,"电池异常移动"),
    BTY_VOLTAGE_WARNING(7, "异常警告"),
    BTY_DISTORY(8,"电池被破坏"),
    BTY_DISTORY_SERVICE(9,"发送电池破换信息至客服"),
    BTY_MOVING_REMIND(10,"布防未关提醒"),
    BTY_WARN_FIRE(11,"电池着火预警");
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
