package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sam.yh.crud.exception.SmsCodeSendException;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserCodeService;

@RestController
@RequestMapping("/user")
public class UserSignupController {

    private static final Logger logger = LoggerFactory.getLogger(UserSignupController.class);

    @Autowired
    UserCodeService userCodeService;

    @RequestMapping("/getsalt")
    public SamResponse getSalt() {
        // TODO
        return null;
    }

    @RequestMapping(value = "/sendmsg", method = RequestMethod.POST)
    public SamResponse sendSmsCode(HttpServletRequest httpServletRequest, String userName) {

        SamResponse resp = new SamResponse();
        try {
            userCodeService.sendAndSaveSmsCode(userName);
        } catch (Exception e) {
            logger.error("短信发送失败，", e);
            if (e instanceof SmsCodeSendException) {
                resp.setData("无法发送短信");
            } else {
                resp.setResCode(SamResponse.RESCODE_UNKNOW_EXCEPTION);
            }
        }
        return resp;
    }

    @RequestMapping("/signup")
    public String signup(HttpServletRequest httpServletRequest, String userName, String hashPwd) {
        // TODO
        return null;
    }
}
