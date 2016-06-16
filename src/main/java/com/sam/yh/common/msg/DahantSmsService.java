package com.sam.yh.common.msg;

public interface DahantSmsService {
    public boolean sendSignupAuthCode(String mobilePhone, String authCode);

    public boolean sendResetPwdAuthCode(String mobilePhone, String authCode);

    public boolean sendTestSms(String mobilePhone, String content);

    public boolean sendLogResellerSuccess(String mobilePhone, String initPwd);

    public boolean sendLogResellerSuccess(String mobilePhone);

    public boolean sendBuyInfo(String mobilePhone);

    public boolean sendWarningMsg(String mobilePhone, String btyImei);

    public boolean sendVoltageWarningMsg(String mobilePhone, String btyImei, String voltage,int flag);

    public boolean sendMovingMsg(String mobilePhone, String btyImei);

    public String getSms();

}
