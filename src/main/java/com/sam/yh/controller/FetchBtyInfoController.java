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
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.req.bean.FetchBtyInfoReq;
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

    @RequestMapping(value = "/getbtyinfo", method = RequestMethod.POST)
    public SamResponse fetchBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.debug("Request json String:" + jsonReq);

        FetchBtyInfoReq req = JSON.parseObject(jsonReq, FetchBtyInfoReq.class);

        SamResponse resp = new SamResponse();

        UserBtyInfoResp respData = new UserBtyInfoResp();

        List<BatteryInfo> btyInfo = userService.fetchBtyInfo(req.getUserName());

        for (BatteryInfo batteryInfo : btyInfo) {
            respData.getBtyInfo().add(convertToUserBtyInfo(batteryInfo));
        }

        resp.setData(respData);
        return resp;

    }

    private UserBtyInfo convertToUserBtyInfo(BatteryInfo batteryInfo) {
        UserBtyInfo userBtyInfo = new UserBtyInfo();
        userBtyInfo.setBtyId(String.valueOf(batteryInfo.getBatteryId()));
        userBtyInfo.setLongitude(batteryInfo.getLongitude());
        userBtyInfo.setLatitude(batteryInfo.getLatitude());
        return userBtyInfo;
    }

}
