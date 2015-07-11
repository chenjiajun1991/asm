package com.sam.yh.common;

public class SamConstants {

    public static final String PARAM_DIGEST = "digest";

    public static final String PARAM_USERNAME = "username";

    /**
     * 相同类型的验证码一天内最大的发送次数
     */
    public static final int MXA_SMS_SEND_TIME = 10;

    /**
     * 电池信息异常一天内最大的发送次数
     */
    public static final int MXA_WARNING_SEND_TIME = 3;

    /**
     * 验证码的有效时间（分钟）
     */
    public static final int EXPIRY_TIME = 30;

    /**
     * 用户关注好友电池的最大数量
     */
    public static final int MAX_FOLLOW_COUNT = 10;
}