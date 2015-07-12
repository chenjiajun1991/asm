package com.sam.yh.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.req.bean.SmsAuthCodeReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserCodeService;

@RestController
@RequestMapping("/open")
public class AuthCodeController {

    private static final Logger logger = LoggerFactory.getLogger(AuthCodeController.class);

    @Autowired
    UserCodeService userCodeService;

    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public SamResponse sendSmsCode(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        SmsAuthCodeReq req = JSON.parseObject(jsonReq, SmsAuthCodeReq.class);

        try {
            validateSmsArgs(req);
            int type = Integer.valueOf(req.getAuthType());
            if (type == UserCodeType.SIGNUP_CODE.getType()) {
                if (userCodeService.sendSignupAuthCode(req.getUserPhone())) {
                    return ResponseUtils.getNormalResp("短信已成功发送");
                } else {
                    return ResponseUtils.getErrorResp("短信发送失败");
                }
            } else if (type == UserCodeType.RESETPWD_CODE.getType()) {
                if (userCodeService.sendResetPwdAuthCode(req.getUserPhone())) {
                    return ResponseUtils.getNormalResp("短信已成功发送");
                } else {
                    return ResponseUtils.getErrorResp("短信发送失败");
                }

            } else if (type == UserCodeType.TEST_CODE.getType()) {
                if (userCodeService.sendTestAuthCode(req.getUserPhone())) {
                    return ResponseUtils.getNormalResp("短信已成功发送");
                } else {
                    return ResponseUtils.getErrorResp("短信发送失败");
                }

            } else {
                return ResponseUtils.getErrorResp("不存在的验证码类型");
            }

        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("send sms exception, " + req.getUserPhone(), e);
            if (e instanceof UserSignupException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("send sms exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateSmsArgs(SmsAuthCodeReq smsAuthCodeReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(smsAuthCodeReq.getUserPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }

        if (!StringUtils.isNumeric(smsAuthCodeReq.getAuthType()) || !UserCodeType.isValidType(Integer.parseInt(smsAuthCodeReq.getAuthType()))) {
            throw new IllegalParamsException("无法发送此类型的验证码");
        }

    }
}
