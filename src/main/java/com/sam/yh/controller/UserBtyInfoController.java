package com.sam.yh.controller;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.util.AttributeKey;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

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
import com.sam.yh.common.PowerCalUtil;
import com.sam.yh.common.SamConstants;
import com.sam.yh.model.Battery;
import com.sam.yh.model.PubBatteryInfo;
import com.sam.yh.req.bean.FetchBtyInfoReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.UserBtyInfo;
import com.sam.yh.resp.bean.UserBtyInfoResp;
import com.sam.yh.service.BatteryService;
import com.sam.yh.service.UserService;
import com.sam.yh.service.socket.SamBtyDataHandler;

@RestController
@RequestMapping("/user")
public class UserBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UserBtyInfoController.class);

    @Autowired
    private UserService userService;
    @Autowired
    BatteryService batteryService;

    @RequestMapping(value = "/btyinfo", method = RequestMethod.POST)
    public SamResponse fetchBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        FetchBtyInfoReq req = JSON.parseObject(jsonReq, FetchBtyInfoReq.class);

        try {
            validateUserArgs(req.getUserPhone());

            UserBtyInfoResp respData = new UserBtyInfoResp();

            List<PubBatteryInfo> selfBtys = userService.fetchSelfBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : selfBtys) {
                respData.getSelfBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            List<PubBatteryInfo> friendBtys = userService.fetchFriendsBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : friendBtys) {
                respData.getFriendsBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch bty info exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    @RequestMapping(value = "/btyinfo/real", method = RequestMethod.POST)
    public SamResponse fetchBtyInfoReal(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {

        logger.info("Request json String:" + jsonReq);

        FetchBtyInfoReq req = JSON.parseObject(jsonReq, FetchBtyInfoReq.class);

        try {
            validateUserArgs(req.getUserPhone());

            for (PubBatteryInfo batteryInfo : userService.fetchSelfBtyInfo(req.getUserPhone())) {
                sendReq(batteryInfo.getBytImei());
            }

            for (PubBatteryInfo batteryInfo : userService.fetchFriendsBtyInfo(req.getUserPhone())) {
                sendReq(batteryInfo.getBytImei());
            }

            TimeUnit.SECONDS.sleep(SamConstants.MAX_WAIT_SECONDS);

            UserBtyInfoResp respData = new UserBtyInfoResp();

            List<PubBatteryInfo> selfBtys = userService.fetchSelfBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : selfBtys) {
                respData.getSelfBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            List<PubBatteryInfo> friendBtys = userService.fetchFriendsBtyInfo(req.getUserPhone());
            for (PubBatteryInfo batteryInfo : friendBtys) {
                respData.getFriendsBtyInfo().add(convertToUserBtyInfo(batteryInfo));
            }

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (Exception e) {
            logger.error("fetch bty info exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }

    }

    private void validateUserArgs(String userPhone) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(userPhone)) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }

    }

    private UserBtyInfo convertToUserBtyInfo(PubBatteryInfo pubBatteryInfo) {
        UserBtyInfo userBtyInfo = new UserBtyInfo();
        userBtyInfo.setOwnerPhone(pubBatteryInfo.getOwnerPhone());
        userBtyInfo.setBtyPubSn(pubBatteryInfo.getBtyPubSn());
        userBtyInfo.setBtyImei(pubBatteryInfo.getBytImei());
        userBtyInfo.setLongitude(pubBatteryInfo.getLongitude());
        userBtyInfo.setLatitude(pubBatteryInfo.getLatitude());
        userBtyInfo.setTemperature(pubBatteryInfo.getTemperature());
        userBtyInfo.setVoltage(pubBatteryInfo.getVoltage());
        Battery battery = batteryService.fetchBtyById(pubBatteryInfo.getBatteryId());
        userBtyInfo.setPower(PowerCalUtil.calPower(pubBatteryInfo.getVoltage(), battery.getBtyQuantity()));
        return userBtyInfo;
    }

    private void sendReq(String btyimei) {
        Battery battery = batteryService.fetchBtyByIMEI(btyimei);
        if (battery == null) {
            logger.error("电池不存在, " + btyimei);
            return;
        }
        boolean hasConn = false;
        ChannelGroup channelGroup = SamBtyDataHandler.getChannels();
        for (Channel c : channelGroup) {
            String imei = (String) c.attr(AttributeKey.valueOf("IMEI")).get();
            logger.info("已经连接的imei：" + imei);
            if (imei != null && imei.equals(battery.getImei())) {
                c.writeAndFlush("tellme" + imei + "\n");
                hasConn = true;
            }

        }
        if (!hasConn) {
            logger.error("未获取到长连接, " + btyimei);
        }
    }

}
