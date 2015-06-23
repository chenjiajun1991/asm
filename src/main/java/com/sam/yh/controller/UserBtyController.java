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
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.BtyFollowException;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.req.bean.BtyFollowReq;
import com.sam.yh.req.bean.BtyShareReq;
import com.sam.yh.req.bean.IllegalReqParamsException;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserService;

@RestController
@RequestMapping("/user")
public class UserBtyController {

    private static final Logger logger = LoggerFactory.getLogger(UserBtyController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/bty/follow", method = RequestMethod.POST)
    public SamResponse followBty(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);

        BtyFollowReq req = JSON.parseObject(jsonReq, BtyFollowReq.class);

        try {
            validateBtyFollowArgs(req);

            userService.followBty(req.getUserPhone(), req.getBtyPubSn(), req.getBtyOwnerPhone());

            return ResponseUtils.getNormalResp(StringUtils.EMPTY);
        } catch (IllegalReqParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("follow bty exception, " + req.getUserPhone(), e);
            if (e instanceof BtyFollowException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("follow bty exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    @RequestMapping(value = "/bty/share", method = RequestMethod.POST)
    public SamResponse shareBty(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);

        BtyShareReq req = JSON.parseObject(jsonReq, BtyShareReq.class);

        try {
            validateBtyShareArgs(req);

            userService.shareBty(req.getUserPhone(), req.getBtyPubSn(), req.getFriendPhone());

            return ResponseUtils.getNormalResp(StringUtils.EMPTY);
        } catch (IllegalReqParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("share bty exception, " + req.getUserPhone(), e);
            if (e instanceof BtyFollowException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("share bty exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateBtyFollowArgs(BtyFollowReq btyFollowReq) throws IllegalReqParamsException {
        if (!MobilePhoneUtils.isValidPhone(btyFollowReq.getUserPhone())) {
            throw new IllegalReqParamsException("请输入正确的手机号码");
        }
        if (StringUtils.isBlank(btyFollowReq.getBtyPubSn())) {
            throw new IllegalReqParamsException("不存在的电池");
        }
        if (!MobilePhoneUtils.isValidPhone(btyFollowReq.getBtyOwnerPhone())) {
            throw new IllegalReqParamsException("请输入好友正确的手机号码");
        }
        if (StringUtils.equals(btyFollowReq.getUserPhone(), btyFollowReq.getBtyOwnerPhone())) {
            throw new IllegalReqParamsException("不能关注自己");
        }
    }

    private void validateBtyShareArgs(BtyShareReq btyShareReq) throws IllegalReqParamsException {
        if (!MobilePhoneUtils.isValidPhone(btyShareReq.getUserPhone())) {
            throw new IllegalReqParamsException("请输入正确的手机号码");
        }
        if (StringUtils.isBlank(btyShareReq.getBtyPubSn())) {
            throw new IllegalReqParamsException("不存在的电池");
        }
        if (!MobilePhoneUtils.isValidPhone(btyShareReq.getFriendPhone())) {
            throw new IllegalReqParamsException("请输入好友正确的手机号码");
        }
        if (StringUtils.equals(btyShareReq.getUserPhone(), btyShareReq.getFriendPhone())) {
            throw new IllegalReqParamsException("不能关注自己");
        }
    }

}
