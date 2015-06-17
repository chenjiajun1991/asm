package com.sam.yh.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.model.PubBatteryInfo;
import com.sam.yh.req.bean.FetchBtyInfoReq;
import com.sam.yh.req.bean.IllegalRepParamsException;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.UserBtyInfo;
import com.sam.yh.resp.bean.UserBtyInfoResp;
import com.sam.yh.service.UserService;

@RestController
@RequestMapping("/user")
public class FetchBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FetchBtyInfoController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/btyinfo", method = RequestMethod.POST)
    public SamResponse fetchBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        FetchBtyInfoReq req = JSON.parseObject(jsonReq, FetchBtyInfoReq.class);

        try {

            validateUserArgs(req);

            SamResponse resp = new SamResponse();
            UserBtyInfoResp respData = new UserBtyInfoResp();

            List<PubBatteryInfo> selfBtys = userService.fetchSelfBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : selfBtys) {
                respData.getSelfBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            List<PubBatteryInfo> friendBtys = userService.fetchFriendsBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : friendBtys) {
                respData.getFriendsfBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            resp.setData(respData);
            return resp;
        } catch (IllegalRepParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch bty info exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validateUserArgs(FetchBtyInfoReq fetchBtyInfoReq) throws IllegalRepParamsException {
        if (!MobilePhoneUtils.isValidPhone(fetchBtyInfoReq.getUserPhone())) {
            throw new IllegalRepParamsException("请输入正确的手机号码");
        }

    }

    private UserBtyInfo convertToUserBtyInfo(PubBatteryInfo pubBatteryInfo) {
        UserBtyInfo userBtyInfo = new UserBtyInfo();
        userBtyInfo.setOwnerPhone(pubBatteryInfo.getOwnerPhone());
        userBtyInfo.setBtyPubSn(pubBatteryInfo.getBtyPubSn());
        userBtyInfo.setLongitude(pubBatteryInfo.getLongitude());
        userBtyInfo.setLatitude(pubBatteryInfo.getLatitude());
        return userBtyInfo;
    }

}
