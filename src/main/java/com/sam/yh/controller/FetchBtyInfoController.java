package com.sam.yh.controller;

import java.text.SimpleDateFormat;

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
import com.sam.yh.crud.exception.FetchBtyInfoException;
import com.sam.yh.model.BatteryInfo;
import com.sam.yh.req.bean.BtyInfoReq;
import com.sam.yh.resp.bean.BtyInfoResp;
import com.sam.yh.resp.bean.ResponseUtils;
import com.sam.yh.resp.bean.SamResponse;
import com.sam.yh.service.BatteryService;

@RestController
@RequestMapping("/bty")
public class FetchBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(FetchBtyInfoController.class);

    @Autowired
    BatteryService batteryService;

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public SamResponse fetchBtyInfo(HttpServletRequest httpServletRequest, @RequestParam("jsonReq") String jsonReq) {
        logger.debug("Request json String:" + jsonReq);
        BtyInfoReq req = JSON.parseObject(jsonReq, BtyInfoReq.class);

        try {
            validateBtyArgs(req);

            BatteryInfo info = batteryService.fetchBtyInfo(req.getBtySimNo());
            BtyInfoResp respData = new BtyInfoResp();
            respData.setLatitude(info.getLatitude());
            respData.setLongitude(info.getLongitude());
            respData.setTemperature(info.getTemperature());
            respData.setVoltage(info.getVoltage());

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(info.getReceiveDate());
            respData.setLastestDate(dateString);

            return ResponseUtils.getNormalResp(respData);
        } catch (IllegalParamsException e) {
            return ResponseUtils.getParamsErrorResp(e.getMessage());
        } catch (CrudException e) {
            logger.error("fetch battery info exception, " + req.getBtySimNo(), e);
            if (e instanceof FetchBtyInfoException) {
                return ResponseUtils.getServiceErrorResp(e.getMessage());
            } else {
                return ResponseUtils.getSysErrorResp();
            }
        } catch (Exception e) {
            logger.error("fetch battery info exception, " + req.getBtySimNo(), e);
            return ResponseUtils.getSysErrorResp();
        }
    }

    private void validateBtyArgs(BtyInfoReq btyInfoReq) throws IllegalParamsException {
        if (!MobilePhoneUtils.isValidPhone(btyInfoReq.getBtySimNo())) {
            throw new IllegalParamsException("请输入正确的电池sim卡号");
        }
    }
}
