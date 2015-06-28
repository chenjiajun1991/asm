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
import com.sam.yh.common.IllegalParamsException;
import com.sam.yh.common.MobilePhoneUtils;
import com.sam.yh.crud.exception.CrudException;
import com.sam.yh.crud.exception.FetchBtysException;
import com.sam.yh.model.PubBattery;
import com.sam.yh.req.bean.FetchBtyInfoReq;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.resp.bean.UserBtysResp;
import com.sam.yh.service.UserBatteryService;

@RestController
@RequestMapping("/user")
public class ListUserBtysController {

    private static final Logger logger = LoggerFactory.getLogger(ListUserBtysController.class);

    @Autowired
    private UserBatteryService userBatteryService;

    @RequestMapping(value = "/btys", method = RequestMethod.POST)
    public SamResponse fetchBtys(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);

        FetchBtyInfoReq req = JSON.parseObject(jsonReq, FetchBtyInfoReq.class);

        try {
            validateUserArgs(req.getUserPhone());

            UserBtysResp respData = new UserBtysResp();

            List<PubBattery> myBtys = userBatteryService.fetchMyBtys(req.getUserPhone());
            respData.setMyBtys(myBtys);

            List<PubBattery> friendBtys = userBatteryService.fetchfriendBtys(req.getUserPhone());
            respData.setFriendBtys(friendBtys);

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch my btys exception, " + req.getUserPhone(), e);
            if (e instanceof FetchBtysException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("fetch my byts exception, " + req.getUserPhone(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateUserArgs(String userPhone) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(userPhone)) {
            throw new IllegalParamsException("请输入正确的手机号码");
        }

    }

}
