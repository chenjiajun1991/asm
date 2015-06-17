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

public class SmsSendUtils {

    private static final Logger logger = LoggerFactory.getLogger(SmsSendUtils.class);

    private static final String SERVERURL = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_SERVERURL);
    private static final String ACCOUNT = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_ACCOUNT);
    private static final String PASSWORD = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_PASSWORD);
    private static final String SIGN = ConfigUtils.getConfig().getString(ConfigUtils.DAHANT_SIGN);
    private static final boolean SMS_ENABLE = ConfigUtils.getConfig().getBoolean(ConfigUtils.SMS_ENABLE);

    public static boolean sendAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "[测试短信]您的注册验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendBuyInfo(String mobilePhone) {

        // TODO
        String content = "您的注册验证码为123456.";
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

}
