package com.sam.yh.common.msg;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UmsClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(UmsClientUtils.class);

    private UmsClientUtils() {

    }

    public static boolean sendSms(final String mobilePhone, String content) {
        boolean result = false;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://sms.api.ums86.com:8899/sms/Api/Send.do");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("SpCode", "103906"));
        nvps.add(new BasicNameValuePair("LoginName", "fj_hdl"));
        nvps.add(new BasicNameValuePair("Password", "SAMworld888"));
        nvps.add(new BasicNameValuePair("MessageContent", content));
        nvps.add(new BasicNameValuePair("UserNumber", mobilePhone));
        nvps.add(new BasicNameValuePair("SerialNumber", String.valueOf(System.currentTimeMillis())));
        nvps.add(new BasicNameValuePair("f", "1"));

        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "GBK"));
            response = httpclient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String repContent = EntityUtils.toString(entity, Charset.forName("GBK"));
                logger.info(repContent);

                result = true;
            }
        } catch (Exception e) {
            logger.error("{}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {

                }
            }

        }
        return result;
    }

    public static void main(String[] args) {
        // sendSms("15618672987", "您的验证码为" + "123459");
        sendSms("15618672987", "你有一项编号为" + "12345678" + "的事务需要处理");

    }

}
