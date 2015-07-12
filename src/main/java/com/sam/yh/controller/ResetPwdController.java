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
import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.AuthCodeVerifyException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.req.bean.UserPwdResetReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserService;

@RestController
@RequestMapping("/user")
public class ResetPwdController {

    private static final Logger logger = LoggerFactory.getLogger(ResetPwdController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public SamResponse resetUserPwd(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        UserPwdResetReq req = JSON.parseObject(jsonReq, UserPwdResetReq.class);

        try {
            validatePwdResetArgs(req);

            userService.resetPwd(req.getUserPhone(), req.getAuthCode(), req.getPassword1(), req.getDeviceInfo());

            return ResponseUtils.getNormalResp(StringUtils.EMPTY);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("reset user password exception, " + req.getUserPhone(), e);
            if (e instanceof UserSignupException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else if (e instanceof AuthCodeVerifyException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("reset user password exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validatePwdResetArgs(UserPwdResetReq userPwdResetReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(userPwdResetReq.getUserPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }

        if (StringUtils.isBlank(userPwdResetReq.getPassword1()) || StringUtils.isBlank(userPwdResetReq.getPassword2())) {
            throw new IllegalParamsException("密码不能为空");
        }

        if (!PwdUtils.isValidPwd(userPwdResetReq.getPassword1())) {
            throw new IllegalParamsException("密码长度为8-20位字符");
        }

        if (!StringUtils.equals(userPwdResetReq.getPassword1(), userPwdResetReq.getPassword2())) {
            throw new IllegalParamsException("密码输入不一致");
        }

    }

}
