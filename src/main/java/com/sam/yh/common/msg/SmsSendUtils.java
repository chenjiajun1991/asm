package com.sam.yh.common.msg;

import java.io.StringReader;

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

    public static boolean sendAuthCode(String mobilePhone, String authCode) {
        // TODO
        String content = "您的注册验证码为" + authCode;
        return sendSms(mobilePhone, content);
    }

    public static boolean sendBuyInfo(String mobilePhone) {

        // TODO
        String content = "您的注册验证码为123456.";
        return sendSms(mobilePhone, content);
    }

    private static boolean sendSms(String mobilePhone, String content) {
        WebServiceXmlClientUtil.setServerUrl(SERVERURL);
        logger.debug("send sms to " + mobilePhone + ", content:" + content);
        String respInfo = WebServiceXmlClientUtil.sendSms(ACCOUNT, PASSWORD, StringUtils.EMPTY, mobilePhone, content, SIGN, StringUtils.EMPTY,
                StringUtils.EMPTY);

        SmsSendResp resp = parseRespInfo(respInfo);
        if (resp == null || resp.getResult() != 0) {
            logger.error("resp:" + respInfo);
            return false;
        } else {
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

    public static void main(String[] args) {
        sendBuyInfo("15618672987");
    }

}
