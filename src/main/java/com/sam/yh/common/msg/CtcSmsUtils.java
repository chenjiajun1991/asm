package com.sam.yh.common.msg;

import java.io.StringReader;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.smscloud.xml.webservice.utils.WebServiceXmlClientUtil;
import com.sam.yh.common.ConfigUtils;

public class CtcSmsUtils {

    private static final Logger logger = LoggerFactory.getLogger(CtcSmsUtils.class);

    private static final String SERVERURL = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_SERVERURL);
    private static final String ACCOUNT = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_ACCOUNT);
    private static final String PASSWORD = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_PASSWORD);
    private static final boolean SMS_ENABLE = ConfigUtils.getConfig().getBoolean(ConfigUtils.SMS_ENABLE);
    private static final String SHORT_URL = ConfigUtils.getConfig().getString(ConfigUtils.ANDRIOD_LATEST_SHORTURL);

    public static boolean sendSignupAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在注册为亚亨蓄电池会员，注册验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendResetPwdAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在重置亚亨蓄电池会员密码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendTestSms(String mobilePhone, String authCode) {
        // TODO
        String content = "您正在测试亚亨蓄电池短信验证码，验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendLogResellerSuccess(String mobilePhone, String initPwd) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，初始密码为" + initPwd + "，请点击下载" + SHORT_URL;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendLogResellerSuccess(String mobilePhone) {
        // TODO
        String content = "恭喜您已经成为亚亨蓄电池经销商，请登录APP查看。";
        return sendSms(mobilePhone, content);
    }

    public static boolean sendBuyInfo(String mobilePhone) {
        // TODO
        String content = "您购买的亚亨蓄电池已经成功录入系统，请您下载APP并跟踪，请点击下载" + SHORT_URL;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendWarningMsg(String mobilePhone, String btyImei) {
        // TODO
        String content = "您的电池IMEI" + btyImei + "温度或电压出现异常，请登录APP查看。";
        return sendSms(mobilePhone, content);
    }

    private static boolean sendSms(final String mobilePhone, String content) {
        logger.info("send sms to " + mobilePhone + ", content:" + content);
        if (!SMS_ENABLE) {
            return true;
        }

        WebServiceXmlClientUtil.setServerUrl(SERVERURL);
        String respInfo = WebServiceXmlClientUtil.sendSms(ACCOUNT, PASSWORD, StringUtils.EMPTY, mobilePhone, content, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY);

        final SmsSendResp resp = parseRespInfo(respInfo);
        if (resp == null || resp.getResult() != 0) {
            logger.error("resp:" + respInfo);
            return false;
        } else {
            Runnable runnable = new Runnable() {

                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(30L);
                        getReport(mobilePhone, resp.getMsgid());
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            };

            new Thread(runnable).start();
            return true;
        }
    }

    private static SmsSendResp parseRespInfo(String respInfo) {
        SmsSendResp resp = null;
        try {
            JAXBContext context = JAXBContext.newInstance(SmsSendResp.class);
            Unmarshaller un = context.createUnmarshaller();
            StringReader sr = new StringReader(respInfo);
            resp = (SmsSendResp) un.unmarshal(sr);
        } catch (JAXBException e) {
            logger.error("failed parse xml String" + respInfo, e);
        }
        return resp;
    }

    private static String getReport(String mobilePhone, String msgid) {
        WebServiceXmlClientUtil.setServerUrl(SERVERURL);
        logger.info("send sms to " + mobilePhone + ", msgid:" + msgid);
        String respInfo = WebServiceXmlClientUtil.getReport(ACCOUNT, PASSWORD, msgid, mobilePhone);
        logger.info(respInfo);

        return respInfo;
    }

    public static String getSms() {
        WebServiceXmlClientUtil.setServerUrl(SERVERURL);
        logger.info("get user repaly sms");
        String respInfo = WebServiceXmlClientUtil.getSms(ACCOUNT, PASSWORD);
        logger.info(respInfo);

        return respInfo;
    }

}
