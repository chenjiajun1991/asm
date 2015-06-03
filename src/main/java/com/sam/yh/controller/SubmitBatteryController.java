package com.sam.yh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sam.yh.req.bean.vo.SubmitBatteryReqVo;

@Controller
public class SubmitBatteryController {

    public static final Logger logger = LoggerFactory.getLogger(SubmitBatteryController.class);

    @RequestMapping(value = "/submitbattery", method = RequestMethod.POST)
    public String submitBattery(@RequestBody final SubmitBatteryReqVo submitBatteryReqVo) {
        logger.info("----------");
        return null;
    }
}
