package com.sam.yh.common.msg;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("defaultUmsSmsService")
public class UmsSmsServiceImpl implements DahantSmsService {

    private static final Logger logger = LoggerFactory.getLogger(UmsSmsServiceImpl.class);

    @Resource
    private String umsSpCode;
    @Resource
    private String umsUserName;
    @Resource
    private String umsPassword;
    @Resource
    private Boolean smsEnable;
    @Resource
    private String apkShortUrl;

    @Override
    public boolean sendSignupAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在注册为亚亨蓄电池会员，注册验证码为" + "，请在30分钟内使用！";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendResetPwdAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在重置亚亨蓄电池会员密码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendTestSms(String mobilePhone, String content) {
        // TODO
        // String content = "您正在测试亚亨蓄电池短信验证码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendLogResellerSuccess(String mobilePhone, String initPwd) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，初始密码为" + initPwd + "，请点击下载" + apkShortUrl;
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendLogResellerSuccess(String mobilePhone) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，请登录APP查看。";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendBuyInfo(String mobilePhone) {
        // TODO
        String content = "您购买的亚亨蓄电池已经成功录入系统，请您下载APP并跟踪，请点击下载" + apkShortUrl + "，回复TD拒收";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendWarningMsg(String mobilePhone, String btyImei) {
        // TODO
        String content = "您的电池IMEI" + btyImei + "温度或电压出现异常，请登录APP查看。";
        return sendSms(mobilePhone, content);
    }

    @Override
    public boolean sendMovingMsg(String mobilePhone, String btyImei) {
        // TODO
        String content = "您的电池IMEI" + btyImei + "设置位置锁定后发生异常移动，请登录APP查看最新地点。如您忘记关闭布防，请及时关闭。";
        return sendSms(mobilePhone, content);
    }

    private boolean sendSms(final String mobilePhone, String content) {
        logger.info("send sms to " + mobilePhone + ", content:" + content);
        if (!smsEnable) {
            return true;
        }

        return UmsClientUtils.sendSms(umsSpCode, umsUserName, umsPassword, mobilePhone, content);

    }

    @Override
    public String getSms() {
        return "";
    }

}
