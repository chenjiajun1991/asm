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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.UserSignupException;
import com.sam.yh.enums.UserCodeType;
import com.sam.yh.req.bean.SysSaltReq;
import com.sam.yh.req.bean.UserSignupReq;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.SysSaltResp;
import com.sam.yh.service.UserCodeService;
import com.sam.yh.service.UserService;

@RestController
@RequestMapping("/user")
public class UserSignupController {

    private static final Logger logger = LoggerFactory.getLogger(UserSignupController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserCodeService userCodeService;

    @RequestMapping(value = "/getsalt", method = RequestMethod.POST)
    public SamResponse getSalt(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        SysSaltReq req = JSON.parseObject(jsonReq, SysSaltReq.class);

        SamResponse resp = new SamResponse();

        String salt = userCodeService.genAndSaveUserSalt(req.getUserName(), UserCodeType.USER_SALT.getType());
        SysSaltResp respObj = new SysSaltResp();
        respObj.setSalt(salt);
        resp.setData(respObj);

        return resp;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public SamResponse signup(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        UserSignupReq req = JSON.parseObject(jsonReq, UserSignupReq.class);

        SamResponse resp = new SamResponse();
        try {
            int userId = userService.signup(req.getUserName(), req.getAuthCode(), req.getHashPwd(), req.getDeviceInfo());
            resp.setData(String.valueOf(userId));
        } catch (CrudException e) {
            if (e instanceof UserSignupException) {
                resp.setData(e.getMessage());
            } else {
                resp.setData("");
                resp.setResCode(SamResponse.RESCODE_UNKNOW_EXCEPTION);
            }
        }

        return resp;
    }
}
