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
    
    //增加一个剪断信号线电压突变报警
    public boolean sendViolentDestroyClient(String mobilePhone);
    
    public boolean sendViolentDestroyService(String mobilePhone,String btyImei,String userName,String userPhone);
    
    public boolean sendMovingRemindMsg(String mobilePhone, String btyImei);
    
    public boolean sendWarnFireMsg(String mobilePhone,String btyImei,String userName,String userPhone);

}
