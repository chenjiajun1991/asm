package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.crud.exception.SmsCodeSendException;
import com.sam.yh.req.bean.SmsAuthCodeReq;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserCodeService;

@RestController
@RequestMapping("/user")
public class AuthCodeController {

    private static final Logger logger = LoggerFactory.getLogger(AuthCodeController.class);

    @Autowired
    UserCodeService userCodeService;

    @RequestMapping(value = "/sendmsg", method = RequestMethod.POST)
    public SamResponse sendSmsCode(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        SmsAuthCodeReq req = JSON.parseObject(jsonReq, SmsAuthCodeReq.class);

        SamResponse resp = new SamResponse();
        try {
            userCodeService.sendAndSaveSmsCode(req.getUserName(), Integer.valueOf(req.getAuthType()));
            resp.setData("");
        } catch (Exception e) {
            logger.error("短信发送失败，", e);
            if (e instanceof SmsCodeSendException) {
                resp.setData(e.getMessage());
            } else {
                resp.setData("");
                resp.setResCode(SamResponse.RESCODE_UNKNOW_EXCEPTION);
            }
        }
        return resp;
    }
}
