package com.sam.yh.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sam.yh.req.bean.BatteryInfoReq;
import com.sam.yh.service.BatteryService;

@Controller
public class UploadBtyInfoController {

    private static final Logger logger = LoggerFactory.getLogger(UploadBtyInfoController.class);

    @Autowired
    BatteryService batteryService;

    @RequestMapping(value = "/upload/btyinfo", method = { RequestMethod.POST, RequestMethod.GET }, produces = "application/json")
    public @ResponseBody
    String uploadBatteryInfo(HttpServletRequest httpServletRequest, BatteryInfoReq batteryInfoReqVo) throws IOException {

        logger.info("Upload battery info request url:" + httpServletRequest.getRequestURI());
        
        /*Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = httpServletRequest.getHeader(key);
            logger.info("Header key:" + key + ", header value" + value);
        }*/

        logger.info("Upload battery info request:" + batteryInfoReqVo);
        // TODO

        batteryService.uploadBatteryInfo(batteryInfoReqVo);

        return "ok";
    }
}
