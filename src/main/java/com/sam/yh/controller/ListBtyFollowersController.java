package com.sam.yh.controller;

import java.util.List;

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
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchFollowerException;
import com.sam.yh.enums.AppVersionStatus;
import com.sam.yh.req.bean.ListFollowersReq;
import com.sam.yh.resp.bean.BtyFollower;
import com.sam.yh.resp.bean.BtyFollowersResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.UserBatteryService;

@RestController
@RequestMapping("/user")
public class ListBtyFollowersController {

    private static final Logger logger = LoggerFactory.getLogger(ListBtyFollowersController.class);

    @Autowired
    private UserBatteryService userBatteryService;

    @RequestMapping(value = "/bty/followers", method = RequestMethod.POST)
    public SamResponse listFollowers(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.info("Request json String:" + jsonReq);

        ListFollowersReq req = JSON.parseObject(jsonReq, ListFollowersReq.class);

        try {
            AppVersionStatus verStatus = AppVersionUtils.checkVersion(req);
            if (StringUtils.equals(AppVersionStatus.FORCE_UPDATE.getStatus(), verStatus.getStatus())) {
                return ResponseUtils.getForceUpdateResp();
            }

            validateUserBtyArgs(req);

            List<BtyFollower> followers = userBatteryService.fetchBtyFollowers(req.getUserPhone().trim(), req.getBtyPubSn().trim());
            BtyFollowersResp respData = new BtyFollowersResp();
            respData.setFollowers(followers);

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch my byt followers exception, " + req.getUserPhone(), e);
            if (e instanceof FetchFollowerException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("fetch my byt followers exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateUserBtyArgs(ListFollowersReq listFollowersReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(listFollowersReq.getUserPhone())) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }
        if (StringUtils.isBlank(listFollowersReq.getBtyPubSn())) {
            throw new IllegalParamsException("不存在的电池");
        }

    }

}
