package com.sam.yh.common.msg;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.sam.yh.common.ConfigUtils;

@Service
public class FetchReplaySmsTask {

    private static final Logger logger = LoggerFactory.getLogger(FetchReplaySmsTask.class);

    private static final String HOST = ConfigUtils.getConfig().getString(ConfigUtils.MAIL_HOST);
    private static final String SENDER = ConfigUtils.getConfig().getString(ConfigUtils.MAIL_SENDER);
    private static final String USERNAME = ConfigUtils.getConfig().getString(ConfigUtils.MAIL_USERNAME);
    private static final String PASSWORD = ConfigUtils.getConfig().getString(ConfigUtils.MAIL_PASSWORD);
    private static final String REVEIVER = ConfigUtils.getConfig().getString(ConfigUtils.MAIL_REVEIVER);

    public void run() {
        String respInfo = CtcSmsUtils.getSms();
        List<ReplaySms> smsMap = parseResp(respInfo);
        forwardSmsByMail(smsMap);

    }

    private List<ReplaySms> parseResp(String respInfo) {
        SmsReplayResp resp = null;
        try {
            JAXBContext context = JAXBContext.newInstance(SmsReplayResp.class);
            Unmarshaller un = context.createUnmarshaller();
            StringReader sr = new StringReader(respInfo);
            resp = (SmsReplayResp) un.unmarshal(sr);
        } catch (JAXBException e) {
            logger.error("failed parse xml String" + respInfo, e);
        }

        if (resp == null || resp.getResult() != 0) {
            return Collections.emptyList();
        }
        return resp.getSms();
    }

    private void forwardSmsByMail(List<ReplaySms> msgs) {
        if (msgs == null) {
            return;
        }
        for (ReplaySms sms : msgs) {
            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            // 设定mail server
            senderImpl.setHost(HOST);
            senderImpl.setUsername(USERNAME);
            senderImpl.setPassword(PASSWORD);

            Properties prop = new Properties();
            // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.timeout", "25000");// milliseconds
            senderImpl.setJavaMailProperties(prop);
            // 建立邮件消息
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(REVEIVER);
            mailMessage.setFrom(SENDER);
            mailMessage.setSubject(sms.getPhone());
            mailMessage.setText("用户回复消息，时间：" + sms.getDelivertime() + "，内容：" + sms.getContent());

            // 发送邮件
            senderImpl.send(mailMessage);

            logger.info("successfully send mail");
        }
    }

}
