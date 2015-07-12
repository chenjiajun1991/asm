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
import com.sam.yh.common.AppVersionUtils;
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.common.PwdUtils;
import com.sam.yh.crud.exception.AuthCodeVerifyException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.model.User;
import com.sam.yh.req.bean.UserSignupReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.UserInfoResp;
import com.sam.yh.service.UserService;

@RestController
@RequestMapping("/user")
public class UserSignupController {

    private static final Logger logger = LoggerFactory.getLogger(UserSignupController.class);

    @Autowired
    UserService userService;

    /*
     * @RequestMapping(value = "/getsalt", method = RequestMethod.POST) public
     * SamResponse getSalt(HttpServletRequest httpServletRequest,
     * 
     * @RequestParam("jsonReq") String jsonReq) {
     * 
     * logger.debug("Request json String:" + jsonReq);
     * 
     * SysSaltReq req = JSON.parseObject(jsonReq, SysSaltReq.class);
     * 
     * SamResponse resp = new SamResponse();
     * 
     * String salt = userCodeService.genAndSaveUserSalt(req.getUserName(),
     * UserCodeType.USER_SALT.getType()); SysSaltResp respObj = new
     * SysSaltResp(); respObj.setSalt(salt); resp.setData(respObj);
     * 
     * return resp; }
     */

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public SamResponse signup(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        UserSignupReq req = JSON.parseObject(jsonReq, UserSignupReq.class);

        try {
            validateSignupArgs(req);

            User user = userService.signup(req.getUserPhone(), req.getAuthCode(), req.getPassword1(), req.getDeviceInfo());

            UserInfoResp respData = new UserInfoResp();
            respData.setUserUid(user.getUuid());
            respData.setUserType(user.getUserType());

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("signup exception, " + req.getUserPhone(), e);
            if (e instanceof UserSignupException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else if (e instanceof AuthCodeVerifyException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("signup exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validateSignupArgs(UserSignupReq userSignupReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(userSignupReq.getUserPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }

        if (StringUtils.isBlank(userSignupReq.getPassword1()) || StringUtils.isBlank(userSignupReq.getPassword2())) {
            throw new IllegalParamsException("密码不能为空");
        }

        if (!PwdUtils.isValidPwd(userSignupReq.getPassword1())) {
            throw new IllegalParamsException("密码长度为8-20位字符");
        }

        if (!StringUtils.equals(userSignupReq.getPassword1(), userSignupReq.getPassword2())) {
            throw new IllegalParamsException("密码输入不一致");
        }

    }
}
