package com.sam.yh.common.msg;

import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class FetchReplaySmsTask {

    private static final Logger logger = LoggerFactory.getLogger(FetchReplaySmsTask.class);

    @Resource
    private String mailHost;
    @Resource
    private String mailSender;
    @Resource
    private String mailUserName;
    @Resource
    private String mailPassword;
    @Resource
    private String mailReceiver;

    @Resource
    private DahantSmsService defaultUmsSmsService;

    public void run() {
        String respInfo = defaultUmsSmsService.getSms();
        if (StringUtils.isNotBlank(respInfo)) {
            List<ReplaySms> smsMap = parseResp(respInfo);
            forwardSmsByMail(smsMap);
        }

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
            senderImpl.setHost(mailHost);
            senderImpl.setUsername(mailUserName);
            senderImpl.setPassword(mailPassword);

            Properties prop = new Properties();
            // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.timeout", "25000");// milliseconds
            senderImpl.setJavaMailProperties(prop);
            // 建立邮件消息
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setTo(mailReceiver);
            mailMessage.setFrom(mailSender);
            mailMessage.setSubject(sms.getPhone());
            mailMessage.setText("用户回复消息，时间：" + sms.getDelivertime() + "，内容：" + sms.getContent());

            // 发送邮件
            senderImpl.send(mailMessage);

            logger.info("successfully send mail");
        }
    }

}
